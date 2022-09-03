package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.SpecificTechnology
import com.sortinghat.backend.data_collector.domain.DockerCompose
import com.sortinghat.backend.data_collector.domain.DockerContainer
import com.sortinghat.backend.data_collector.exceptions.UnableToConvertDataException
import com.sortinghat.backend.domain.model.Database
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
        val services = converter.execute(dockerCompose)
        val databases = services.fold(setOf<Database>()) { dbs, service -> dbs.plus(service.databasesUsages) }

        assertEquals(1, services.size)
        assertEquals("app", services.first().name)
        assertEquals(1, databases.size)
        assertEquals("db", databases.first().namespace)
    }

    @Test
    fun `it throws an error when converting fails`() {
        val specificTechnology = mock(SpecificTechnology::class.java)
        assertThrows<UnableToConvertDataException> { DockerComposeToModel().execute(specificTechnology) }
    }
}
