package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.OpenApi
import com.sortinghat.backend.data_collector.factories.CollectionComponentsFactory
import com.sortinghat.backend.data_collector.payloads.ServiceAndOpenApi
import com.sortinghat.backend.data_collector.payloads.ServicesEndpointsRegistrationPayload
import com.sortinghat.backend.data_collector.utils.FetchResponse
import com.sortinghat.backend.domain.exceptions.EntityNotFoundException
import com.sortinghat.backend.domain.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class RegisterServicesEndpointsImplTest {
    @Mock private lateinit var fetcher: DataFetcher
    @Mock private lateinit var parser: DataParser
    @Mock private lateinit var converter: ConverterToDomain<Operation>
    @Mock private lateinit var factory: CollectionComponentsFactory
    @Mock private lateinit var repository: ServiceRepository

    private val registerServicesEndpoints by lazy { RegisterServicesEndpointsImpl(factory, repository) }

    @BeforeEach
    fun initFactory() {
        `when`(factory.createDataFetcher()).thenReturn(fetcher)
        `when`(factory.createDataParser()).thenReturn(parser)
        `when`(factory.createConverterToDomain<Operation>()).thenReturn(converter)
    }

    @Test
    fun `it should throw an exception when there is no system with the name informed`() {
        `when`(repository.findAllBySystem("codepix")).thenReturn(setOf())
        val payload = ServicesEndpointsRegistrationPayload(
            repoUrl = "https://github.com/erickrodrigs/codepix",
            servicesAndOpenApiFilenames = listOf()
        )
        assertThrows<EntityNotFoundException> {
            registerServicesEndpoints.execute("codepix", payload)
        }
    }

    @Test
    fun `it should throw an exception when service does not exist in the system`() {
        `when`(repository.findAllBySystem("codepix")).thenReturn(
            setOf(
                Service(
                    name = "payment-service",
                    responsibility = "",
                    module = Module("payment-service"),
                    system = ServiceBasedSystem("codepix", "")
                )
            )
        )
        val payload = ServicesEndpointsRegistrationPayload(
            repoUrl = "https://github.com/erickrodrigs/codepix",
            servicesAndOpenApiFilenames = listOf(
                ServiceAndOpenApi(serviceName = "user-service", "user-openapi.yaml")
            )
        )
        assertThrows<EntityNotFoundException> {
            registerServicesEndpoints.execute("codepix", payload)
        }
    }

    @Nested
    @DisplayName("when services endpoints registration is successful")
    inner class SuccessfulServicesEndpointsRegistration {
        private val operations = setOf(Operation(HttpVerb.GET, "/users"), Operation(HttpVerb.POST, "/users"))
        private val service = Service(
            name = "user-service",
            responsibility = "",
            module = Module("user-service"),
            system = ServiceBasedSystem("codepix", "")
        )

        @BeforeEach
        fun init() {
            val openApi = OpenApi(
                paths = mapOf(
                    "/users" to mapOf(
                        "get" to Any(),
                        "post" to Any()
                    )
                )
            )
            val fetchResponse = FetchResponse("codepix", "")

            `when`(fetcher.execute("https://github.com/erickrodrigs/codepix", "user-openapi.yaml"))
                .thenReturn(fetchResponse)
            `when`(parser.execute(fetchResponse)).thenReturn(openApi)
            `when`(converter.execute(openApi)).thenReturn(operations)
            `when`(repository.findAllBySystem("codepix")).thenReturn(setOf(service))

            val payload = ServicesEndpointsRegistrationPayload(
                repoUrl = "https://github.com/erickrodrigs/codepix",
                servicesAndOpenApiFilenames = listOf(
                    ServiceAndOpenApi(serviceName = "user-service", "user-openapi.yaml")
                )
            )

            registerServicesEndpoints.execute("codepix", payload)
        }

        @Test
        fun `it should put the operations in the services`() {
            assertEquals(operations, service.exposedOperations)
        }

        @Test
        fun `it should save all services with operations in the database`() {
            verify(repository, Mockito.times(1)).saveAll(setOf(service))
        }
    }
}
