package com.thesortinghat.staticcollector.domain.dockercompose

import com.thesortinghat.staticcollector.domain.model.SpecificTechnology
import com.thesortinghat.staticcollector.domain.exceptions.UnableToConvertDataException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock

class DockerComposeToModelTest {
    private val dockerCompose by lazy {
        DockerCompose(name = "my-system")
    }

    @BeforeEach
    fun init() {
        val db = DockerContainer(image = "mongo:4.2")
        val app = DockerContainer(build = "docker/", depends_on = listOf("db"))
        dockerCompose.services = hashMapOf(
            "db" to db,
            "app" to app
        )
    }

    @Test
    fun `it returns a valid system for a given docker compose`() {
        val converter = DockerComposeToModel()
        val system = converter.execute(dockerCompose)
        val databases = system.databases()
        val services = system.services()
        val dbServiceLinks = system.databasesUsages()

        assertEquals(1, databases.size)
        assertEquals("db", databases[0].name)
        assertEquals(1, services.size)
        assertEquals("app", services[0].name)
        assertEquals(1, dbServiceLinks.size)
    }

    @Test
    fun `it throws an error when converting fails`() {
        val specificTechnology = mock(SpecificTechnology::class.java)
        assertThrows<UnableToConvertDataException> { DockerComposeToModel().execute(specificTechnology) }
    }
}