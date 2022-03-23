package com.thesortinghat.staticcollector.application.services

import com.thesortinghat.staticcollector.application.exceptions.EntityAlreadyExistsException
import com.thesortinghat.staticcollector.domain.model.Database
import com.thesortinghat.staticcollector.domain.model.Service
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystemRepository
import com.thesortinghat.staticcollector.domain.services.ExtractData
import com.thesortinghat.staticcollector.infrastructure.kafka.MessageQueue
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any

class RegisterNewSystemTest {

    private val repository by lazy { mock(ServiceBasedSystemRepository::class.java) }
    private val messageQueue by lazy { mock(MessageQueue::class.java) }
    private val extractData by lazy { mock(ExtractData::class.java) }
    private val registerNewSystem by lazy { RegisterNewSystem(extractData, repository, messageQueue) }
    private val system by lazy {
        val service = Service("1", "foo")
        val db = Database("2", "bar", "baz", "baz")
        val system = ServiceBasedSystem("3", "Sorting Hat")
        system.addService(service)
        system.addDatabase(db)
        system.addDatabaseUsage(db, service)
        system
    }

    @Test
    fun `call extract use case for a system which name already exists will throw an exception`() {
        `when`(extractData.execute(anyString(), anyString())).thenReturn(system)
        `when`(repository.findByName(anyString())).thenReturn(system)
        assertThrows(EntityAlreadyExistsException::class.java) {
            registerNewSystem.execute("https://github.com", "docker-compose.yaml")
        }
    }

    @Test
    fun `it works properly for a system which name doesn't exist`() {
        `when`(extractData.execute(anyString(), anyString())).thenReturn(system)
        `when`(repository.findByName(anyString())).thenReturn(null)
        registerNewSystem.execute("https://github.com", "docker-compose.yaml")
        verify(repository, times(1)).save(any())
    }

    @Test
    fun `it sends the collected data to the message queue`() {
        `when`(extractData.execute(anyString(), anyString())).thenReturn(system)
        `when`(repository.findByName(anyString())).thenReturn(null)
        registerNewSystem.execute("https://github.com", "docker-compose.yaml")
        verify(messageQueue, times(1)).newSystem(any())
        verify(messageQueue, times(1)).newService(any())
        verify(messageQueue, times(1)).newDatabase(any())
        verify(messageQueue, times(1)).newUsage(any())
    }
}
