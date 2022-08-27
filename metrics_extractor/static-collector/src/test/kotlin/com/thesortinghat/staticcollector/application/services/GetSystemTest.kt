package com.thesortinghat.staticcollector.application.services

import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystem
import com.thesortinghat.staticcollector.application.exceptions.EntityNotFoundException
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystemRepository
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
    private lateinit var repo: ServiceBasedSystemRepository

    private val getSystem by lazy { GetSystem(repo) }

    @Test
    fun `should return the service based system`() {
        val system = ServiceBasedSystem.create("my-system")
        `when`(repo.findById(system.id.toString())).thenReturn(system)
        assertEquals(system.id, getSystem.execute(system.id.toString()).id)
    }

    @Test
    fun `should throw an exception when system does not exist`() {
        `when`(repo.findById(anyString())).thenReturn(null)
        assertThrows<EntityNotFoundException> { getSystem.execute("1") }
    }
}
