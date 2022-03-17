package com.erickrodrigues.staticcollector.infrastructure.database

import com.erickrodrigues.staticcollector.domain.entities.Database
import com.erickrodrigues.staticcollector.domain.entities.Service
import com.erickrodrigues.staticcollector.domain.entities.ServiceBasedSystem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class SystemMongoDbRepoTest {
    private val repo = mock(SpringDataMongoSystemRepository::class.java)

    @Test
    fun `it returns the domain object when it finds the system in database by name`() {
        val system = System(
            id = "1",
            name = "foo",
            services = listOf(AppService("2", "my-service")),
            databases = listOf(AppDb("3", "my-db", "MongoDB", "NoSQL")),
            linksDbService = listOf(AppLinkDbService("3", "2", "static-collector-db"))
        )
        `when`(repo.findOneByNameIn("foo")).thenReturn(system)
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
        val mongoDbSystem = System(
            id = "1",
            name = "foo",
            services = listOf(AppService("2", "my-service")),
            databases = listOf(AppDb("3", "my-db", "MongoDB", "NoSQL")),
            linksDbService = listOf(AppLinkDbService("3", "2", "static-collector-db"))
        )
        `when`(repo.save(mongoDbSystem)).thenReturn(mongoDbSystem)
        assertDoesNotThrow { SystemMongoDbRepo(repo).save(domainSystem) }
    }
}