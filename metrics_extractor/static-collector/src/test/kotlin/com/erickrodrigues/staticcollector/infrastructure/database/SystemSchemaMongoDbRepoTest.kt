package com.erickrodrigues.staticcollector.infrastructure.database

import com.erickrodrigues.staticcollector.domain.entities.Database
import com.erickrodrigues.staticcollector.domain.entities.Service
import com.erickrodrigues.staticcollector.domain.entities.ServiceBasedSystem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class SystemSchemaMongoDbRepoTest {
    private val repo = mock(SpringDataMongoSystemRepository::class.java)

    @Test
    fun `it returns the domain object when it finds the system in database by name`() {
        val systemSchema = SystemSchema(
            id = "1",
            name = "foo",
            services = listOf(ServiceSchema("2", "my-service")),
            databases = listOf(DbSchema("3", "my-db", "MongoDB", "NoSQL")),
            linksDbService = listOf(LinkDbServiceSchema("3", "2", "static-collector-db"))
        )
        `when`(repo.findOneByNameIn("foo")).thenReturn(systemSchema)
        val systemMongoDbRepo = SystemMongoDbRepo(repo)
        val domainSystem = systemMongoDbRepo.findByName("foo")!!
        assertEquals("1", domainSystem.id)
        assertEquals("foo", domainSystem.name)
        assertEquals(1, domainSystem.services().size)
        assertEquals(1, domainSystem.databases().size)
        assertEquals(1, domainSystem.linksDatabasesServices().size)
        assertEquals("2", domainSystem.linksDatabasesServices()[0].service.id)
        assertEquals("3", domainSystem.linksDatabasesServices()[0].db.id)
    }

    @Test
    fun `it saves the mongodb system object when receives a domain object as parameter`() {
        val service = Service("2", "my-service")
        val database = Database("3", "my-db", "MongoDB", "NoSQL")
        val domainSystem = ServiceBasedSystem("1", "foo")
        domainSystem.addService(service)
        domainSystem.addDatabase(database)
        domainSystem.bindDatabaseToService(database, service, "static-collector-db")
        val mongoDbSystemSchema = SystemSchema(
            id = "1",
            name = "foo",
            services = listOf(ServiceSchema("2", "my-service")),
            databases = listOf(DbSchema("3", "my-db", "MongoDB", "NoSQL")),
            linksDbService = listOf(LinkDbServiceSchema("3", "2", "static-collector-db"))
        )
        `when`(repo.save(mongoDbSystemSchema)).thenReturn(mongoDbSystemSchema)
        assertDoesNotThrow { SystemMongoDbRepo(repo).save(domainSystem) }
    }
}