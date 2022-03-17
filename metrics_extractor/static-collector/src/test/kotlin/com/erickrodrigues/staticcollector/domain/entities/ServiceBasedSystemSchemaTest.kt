package com.erickrodrigues.staticcollector.domain.entities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ServiceBasedSystemSchemaTest {

    @Test
    fun `add service to system`() {
        val service = Service("payment-service")
        val system = ServiceBasedSystem("my-system")
        system.addService(service)
        assertEquals(1, system.services().size)
        assertEquals(service.id, system.services()[0].id)
    }

    @Test
    fun `add database to system`() {
        val database = Database("payment-database", "MongoDB", "NoSQL")
        val system = ServiceBasedSystem("my-system")
        system.addDatabase(database)
        assertEquals(1, system.databases().size)
        assertEquals(database.id, system.databases()[0].id)
    }

    @Test
    fun `bind database to service`() {
        val serviceA = Service("payment-service")
        val serviceB = Service("users-service")
        val database = Database("payment-database", "MongoDB", "NoSQL")
        val system = ServiceBasedSystem("my-system")

        system.bindDatabaseToService(database, serviceA)
        system.bindDatabaseToService(database, serviceB)

        val links = system.linksDatabasesServices()
        val (sA, sB) = links.map { it.service }
        val matchServices = (sA.id == serviceA.id && sB.id == serviceB.id) ||
                (sB.id == serviceA.id && sA.id == serviceB.id)

        assertEquals(2, links.size)
        assertEquals(database.id, links[0].db.id)
        assertEquals(database.id, links[1].db.id)
        assertTrue(matchServices)
    }
}