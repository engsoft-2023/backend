package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.exceptions.EntityAlreadyExistsException
import com.sortinghat.backend.domain.model.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any

class RegisterNewSystemTest {

    private val repository by lazy { mock(ServiceRepository::class.java) }
    private val extractData by lazy { mock(ExtractData::class.java) }
    private val registerNewSystem by lazy { RegisterNewSystem(extractData, repository) }
    private val service by lazy {
        val db = Database("bar", DataSource.MySql)
        val system = ServiceBasedSystem("Sorting Hat", "")
        val service = Service("foo", "", Module("module"), system)

        service.addUsage(db, DatabaseAccessType.ReadWrite)

        service
    }

    @Test
    fun `call extract use case for a system which name already exists will throw an exception`() {
        `when`(extractData.execute(anyString(), anyString())).thenReturn(setOf(service))
        `when`(repository.findAllBySystem(anyString())).thenReturn(setOf(service))
        assertThrows(EntityAlreadyExistsException::class.java) {
            registerNewSystem.execute("https://github.com", "docker-compose.yaml")
        }
    }

    @Test
    fun `it works properly for a system which name doesn't exist`() {
        `when`(extractData.execute(anyString(), anyString())).thenReturn(setOf(service))
        `when`(repository.findAllBySystem(anyString())).thenReturn(setOf())
        registerNewSystem.execute("https://github.com", "docker-compose.yaml")
        verify(repository, times(1)).saveAll(any())
    }
}
