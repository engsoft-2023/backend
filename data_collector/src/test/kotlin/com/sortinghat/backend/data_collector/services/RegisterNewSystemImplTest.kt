package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.DockerCompose
import com.sortinghat.backend.data_collector.exceptions.EntityAlreadyExistsException
import com.sortinghat.backend.data_collector.factories.CollectionComponentsFactory
import com.sortinghat.backend.data_collector.utils.FetchResponse
import com.sortinghat.backend.domain.model.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class RegisterNewSystemImplTest {

    @Mock
    private lateinit var fetcher: DataFetcher

    @Mock
    private lateinit var parser: DataParser

    @Mock
    private lateinit var converter: ConverterToDomain

    @Mock
    private lateinit var factory: CollectionComponentsFactory

    private val repository by lazy { mock(ServiceRepository::class.java) }
    private val registerNewSystem by lazy { RegisterNewSystemImpl(factory, repository) }
    private val services by lazy {
        val db = Database("bar", DataSource.MySql)
        val system = ServiceBasedSystem("Sorting Hat", "")
        val service = Service("foo", "", Module("module"), system)

        service.addUsage(db, DatabaseAccessType.ReadWrite)

        setOf(service)
    }

    @BeforeEach
    fun init() {
        `when`(factory.createDataFetcher()).thenReturn(fetcher)
        `when`(factory.createDataParser()).thenReturn(parser)
        `when`(factory.createConverterToDomain()).thenReturn(converter)
        `when`(fetcher.execute("https://github.com/erickrodrigs/codepix", "docker-compose.yml"))
            .thenReturn(FetchResponse("erickrodrigs/codepix", "some data"))
        `when`(parser.execute(FetchResponse("erickrodrigs/codepix", "some data")))
            .thenReturn(DockerCompose(name = "erickrodrigs/codepix"))
        `when`(converter.execute(DockerCompose(name = "erickrodrigs/codepix"))).thenReturn(services)
    }

    @Test
    fun `it registers the new system successfully`() {
        `when`(repository.findAllBySystem(anyString())).thenReturn(setOf())
        val actual = registerNewSystem.execute("https://github.com/erickrodrigs/codepix", "docker-compose.yml")
        Assertions.assertEquals(1, actual.size)
        Assertions.assertEquals("foo", actual.first().name)
        Assertions.assertEquals("Sorting Hat", actual.first().system.name)
    }

    @Test
    fun `it throws an exception for a system which name already exists`() {
        `when`(repository.findAllBySystem(anyString())).thenReturn(services)
        assertThrows(EntityAlreadyExistsException::class.java) {
            registerNewSystem.execute("https://github.com/erickrodrigs/codepix", "docker-compose.yml")
        }
    }

    @Test
    fun `it saves new system in database for a system which name doesn't exist`() {
        `when`(repository.findAllBySystem(anyString())).thenReturn(setOf())
        registerNewSystem.execute("https://github.com/erickrodrigs/codepix", "docker-compose.yml")
        verify(repository, times(1)).saveAll(any())
    }
}
