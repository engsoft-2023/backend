package com.sortinghat.static_collector.domain.entities.platform_independent_model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

class SystemTest {
    private lateinit var system: System

    @BeforeEach
    fun init() {
        system = System("Sorting Hat")
    }

    @Test
    @DisplayName("check if system has a valid id")
    fun testSystemId() {
        assertDoesNotThrow { UUID.fromString(system.id) }
    }

    @Test
    @DisplayName("add new context, service and database in a system")
    fun testAddContextServiceAndDatabaseVertices() {
        val context = Context("data-collector")
        val service = Service("data-collector")
        val database = Database("data-collector-db", "MongoDB", "NoSQL")

        system.addContext(context)
        system.addService(service)
        system.addDatabase(database)

        assertEquals(context, system.contexts()[0])
        assertEquals(service, system.services()[0])
        assertEquals(database, system.databases()[0])
    }

    @Test
    @DisplayName("binding contexts, services and databases in a system")
    fun testAddContextServiceAndDatabaseEdges() {
        val context = Context("data-collector")
        val service = Service("data-collector")
        val database = Database("data-collector-db", "MongoDB", "NoSQL")

        system.bindDatabaseToContext(database, context)
        system.bindServiceToContext(service, context)
        system.bindDatabaseToService(database, service, "namespace" to "data-collector-db")

        assertEquals(1, system.contexts().size)
        assertEquals(1, system.services().size)
        assertEquals(1, system.databases().size)
    }
}