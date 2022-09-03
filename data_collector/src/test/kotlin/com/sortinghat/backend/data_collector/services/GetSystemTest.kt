package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.exceptions.EntityNotFoundException
import com.sortinghat.backend.domain.model.Module
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.domain.model.ServiceBasedSystem
import com.sortinghat.backend.domain.model.ServiceRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class GetSystemTest {

    @Mock
    private lateinit var repo: ServiceRepository

    private val getSystem by lazy { GetSystem(repo) }

    @Test
    fun `should return the service based system`() {
        val system = ServiceBasedSystem("my-system", "")
        val service = Service("my-service", "", Module("my-module"), system)

        `when`(repo.findAllBySystem(system.name)).thenReturn(setOf(service))

        val systemDto = getSystem.execute(system.name)

        assertEquals(1, systemDto.services.size)
        assertEquals(1, systemDto.modules.size)
        assertEquals(service.name, systemDto.services.first().name)
    }

    @Test
    fun `should throw an exception when system does not exist`() {
        `when`(repo.findAllBySystem(anyString())).thenReturn(setOf())
        assertThrows<EntityNotFoundException> { getSystem.execute("1") }
    }
}
