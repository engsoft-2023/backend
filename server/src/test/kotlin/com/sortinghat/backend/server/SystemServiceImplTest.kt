package com.sortinghat.backend.server

import com.sortinghat.backend.domain.exceptions.EntityNotFoundException
import com.sortinghat.backend.domain.model.Module
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.domain.model.ServiceBasedSystem
import com.sortinghat.backend.domain.model.ServiceRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class SystemServiceImplTest {

    @Mock
    private lateinit var repo: ServiceRepository

    private val systemService by lazy { SystemServiceImpl(repo) }

    @Test
    fun `should return the service based system`() {
        val system = ServiceBasedSystem("my-system", "")
        val service = Service("my-service", "", Module("my-module"), system)

        `when`(repo.findAllBySystem(system.name)).thenReturn(setOf(service))

        val services = systemService.findAllServicesBySystem(system.name)

        assertEquals(1, services.size)
        assertEquals(service.name, services.first().name)
    }

    @Test
    fun `should throw an exception when system does not exist`() {
        `when`(repo.findAllBySystem(anyString())).thenReturn(setOf())
        assertThrows<EntityNotFoundException> { systemService.findAllServicesBySystem("1") }
    }
}
