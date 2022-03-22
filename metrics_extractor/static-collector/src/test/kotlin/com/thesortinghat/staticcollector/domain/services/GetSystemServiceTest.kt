package com.thesortinghat.staticcollector.domain.services

import com.thesortinghat.staticcollector.domain.entities.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.exceptions.EntityNotFoundException
import com.thesortinghat.staticcollector.domain.ports.ServiceBasedSystemRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*

class GetSystemServiceTest {
    private val repo by lazy { mock(ServiceBasedSystemRepository::class.java) }
    private val getSystem by lazy { GetSystemService(repo) }

    @Test
    fun `should return the service based system`() {
        val system = ServiceBasedSystem("my-system")
        `when`(repo.findById(system.id)).thenReturn(system)
        assertEquals(system.id, getSystem.run(system.id).id)
    }

    @Test
    fun `should throw an exception when system does not exist`() {
        `when`(repo.findById(anyString())).thenReturn(null)
        assertThrows<EntityNotFoundException> { getSystem.run("1") }
    }
}
