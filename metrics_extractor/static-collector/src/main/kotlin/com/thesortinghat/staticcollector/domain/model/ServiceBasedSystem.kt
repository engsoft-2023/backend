package com.thesortinghat.staticcollector.domain.model

import java.util.UUID

data class ServiceBasedSystem(val id: String, val name: String) {
    private val graph = Graph()

    constructor(name: String): this(UUID.randomUUID().toString(), name)

    fun addService(s: Service) = graph.addVertex(s)

    fun addDatabase(db: Database) = graph.addVertex(db)

    fun addDatabaseUsage(db: Database, s: Service, payload: Any? = null) = graph.addEdge(DatabaseUsage(db, s, payload))

    fun services() = graph.vertices().filterIsInstance<Service>()

    fun databases() = graph.vertices().filterIsInstance<Database>()

    fun databasesUsages() = graph.edges().filterIsInstance<DatabaseUsage>()
}
