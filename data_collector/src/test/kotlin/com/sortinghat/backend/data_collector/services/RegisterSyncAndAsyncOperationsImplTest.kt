package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.payloads.ServicesSyncAndAsyncOperationsPayload
import com.sortinghat.backend.data_collector.payloads.SyncAndAsyncOperations
import com.sortinghat.backend.domain.exceptions.EntityNotFoundException
import com.sortinghat.backend.domain.model.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class RegisterSyncAndAsyncOperationsImplTest {
    @Mock
    private lateinit var repository: ServiceRepository

    private val registerSyncAndAsyncOperations by lazy { RegisterSyncAndAsyncOperationsImpl(repository) }

    @Test
    fun `it should throw an exception when system is not found`() {
        val systemName = "InterSCity"
        val payload = ServicesSyncAndAsyncOperationsPayload(
            operations = mapOf()
        )
        Mockito.`when`(repository.findAllBySystem(systemName)).thenReturn(setOf())

        assertThrows<EntityNotFoundException> {
            registerSyncAndAsyncOperations.execute(systemName, payload)
        }
    }

    @Test
    fun `it should throw an exception when fromService is not found`() {
        val systemName = "InterSCity"
        val services = setOf(
            Service(
                name = "user-service",
                responsibility = "",
                module = Module("user-service"),
                system = ServiceBasedSystem(systemName, "")
            )
        )
        val payload = ServicesSyncAndAsyncOperationsPayload(
            operations = mapOf(
                "payment-service" to SyncAndAsyncOperations(synchronous = mapOf(), asynchronous = mapOf())
            )
        )
        Mockito.`when`(repository.findAllBySystem(systemName)).thenReturn(services)

        assertThrows<EntityNotFoundException> {
            registerSyncAndAsyncOperations.execute(systemName, payload)
        }
    }

    @Test
    fun `it should throw an exception when toService is not found`() {
        val systemName = "InterSCity"
        val services = setOf(
            Service(
                name = "user-service",
                responsibility = "",
                module = Module("user-service"),
                system = ServiceBasedSystem(systemName, "")
            )
        )
        val payload = ServicesSyncAndAsyncOperationsPayload(
            operations = mapOf(
                "user-service" to SyncAndAsyncOperations(
                    synchronous = mapOf(
                        "payment-service" to "GET /users/{id}/payments"
                    ),
                    asynchronous = mapOf()
                )
            )
        )
        Mockito.`when`(repository.findAllBySystem(systemName)).thenReturn(services)

        assertThrows<EntityNotFoundException> {
            registerSyncAndAsyncOperations.execute(systemName, payload)
        }
    }

    @Test
    fun `it should throw an exception when exposed operation is not found`() {
        val systemName = "InterSCity"
        val services = setOf(
            Service(
                name = "user-service",
                responsibility = "",
                module = Module("user-service"),
                system = ServiceBasedSystem(systemName, ""),
            ),
            Service(
                name = "payment-service",
                responsibility = "",
                module = Module("payment-service"),
                system = ServiceBasedSystem(systemName, ""),
                exposedOperations = mutableSetOf(Operation.fromString("GET /payments"))
            ),
        )
        val payload = ServicesSyncAndAsyncOperationsPayload(
            operations = mapOf(
                "user-service" to SyncAndAsyncOperations(
                    synchronous = mapOf(
                        "payment-service" to "GET /users/{id}/payments"
                    ),
                    asynchronous = mapOf()
                )
            )
        )
        Mockito.`when`(repository.findAllBySystem(systemName)).thenReturn(services)

        assertThrows<EntityNotFoundException> {
            registerSyncAndAsyncOperations.execute(systemName, payload)
        }
    }

    @Test
    fun `it should successfully register services' sync and async operations`() {
        val systemName = "InterSCity"
        val services = setOf(
            Service(
                name = "user-service",
                responsibility = "",
                module = Module("user-service"),
                system = ServiceBasedSystem(systemName, ""),
            ),
            Service(
                name = "payment-service",
                responsibility = "",
                module = Module("payment-service"),
                system = ServiceBasedSystem(systemName, ""),
                exposedOperations = mutableSetOf(Operation.fromString("GET /users/{id}/payments"))
            ),
        )
        val payload = ServicesSyncAndAsyncOperationsPayload(
            operations = mapOf(
                "user-service" to SyncAndAsyncOperations(
                    synchronous = mapOf(
                        "payment-service" to "GET /users/{id}/payments"
                    ),
                    asynchronous = mapOf(
                        "payment-service" to "PAYMENT_MADE"
                    )
                )
            )
        )
        val expectedServices = setOf(
            Service(
                name = "user-service",
                responsibility = "",
                module = Module("user-service"),
                system = ServiceBasedSystem(systemName, ""),
                consumedOperations = mutableSetOf(Operation.fromString("GET /users/{id}/payments")),
                channelsSubscribing = mutableSetOf(MessageChannel("PAYMENT_MADE"))
            ),
            Service(
                name = "payment-service",
                responsibility = "",
                module = Module("payment-service"),
                system = ServiceBasedSystem(systemName, ""),
                exposedOperations = mutableSetOf(Operation.fromString("GET /users/{id}/payments")),
                channelsPublishing = mutableSetOf(MessageChannel("PAYMENT_MADE"))
            ),
        )
        Mockito.`when`(repository.findAllBySystem(systemName)).thenReturn(services)

        registerSyncAndAsyncOperations.execute(systemName, payload)
        verify(repository, times(1)).saveAll(expectedServices)
    }
}
