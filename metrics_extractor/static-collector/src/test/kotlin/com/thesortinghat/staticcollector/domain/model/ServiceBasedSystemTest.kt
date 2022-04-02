package com.thesortinghat.staticcollector.domain.model

import com.thesortinghat.staticcollector.domain.exceptions.DuplicateSystemComponentException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ServiceBasedSystemTest {

    @Test
    fun `throws an exception when creating a system with blank name`() {
        assertThrows<IllegalArgumentException> { ServiceBasedSystem.create("   ") }
    }

    @Test
    fun `throws an exception when adding services with same name`() {
        val system = ServiceBasedSystem.create("amazon")
        system.addService(Service("payment-service"))
        assertThrows<DuplicateSystemComponentException> { system.addService(Service("payment-service")) }
    }

    @Test
    fun `throws an exception when adding services with same id`() {
        val service = Service("payment-service")
        val system = ServiceBasedSystem.create("amazon")
        system.addService(service)
        assertThrows<DuplicateSystemComponentException> {
            system.addService(Service(service.id, "account-service"))
        }
    }

    @Test
    fun `throws an exception when adding databases with same name`() {
        val database = Database("account-db", "MySQL", "SQL")
        val system = ServiceBasedSystem.create("amazon")
        system.addDatabase(database)
        assertThrows<DuplicateSystemComponentException> {
            system.addDatabase(Database("account-db", "MongoDB", "NoSQL"))
        }
    }

    @Test
    fun `throws an exception when adding databases with same id`() {
        val database = Database("account-db", "MySQL", "SQL")
        val system = ServiceBasedSystem.create("amazon")
        system.addDatabase(database)
        assertThrows<DuplicateSystemComponentException> {
            system.addDatabase(Database(database.id,"payment-db", "MongoDB", "NoSQL"))
        }
    }

    @Test
    fun `throws an exception when adding the same database usage`() {
        val system = ServiceBasedSystem.create("amazon")
        val database = Database("payment-db", "MongoDB", "NoSQL")
        val service = Service("payment-service")
        system.addDatabaseUsage(database, service)
        assertThrows<DuplicateSystemComponentException> { system.addDatabaseUsage(database, service) }
    }
}
