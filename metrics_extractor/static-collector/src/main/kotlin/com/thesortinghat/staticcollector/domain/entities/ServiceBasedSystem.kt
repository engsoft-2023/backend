package com.thesortinghat.staticcollector.domain.entities

import java.util.UUID

class ServiceBasedSystem {
    val id: String
    val name: String
    private val graph = Graph()

    constructor(name: String) {
        this.id = UUID.randomUUID().toString()
        this.name = name
    }

    constructor(id: String, name: String) {
        this.id = id
        this.name = name
    }

    fun addService(s: Service) = graph.addVertex(s)

    fun addDatabase(db: Database) = graph.addVertex(db)

    fun bindDatabaseToService(db: Database, s: Service, payload: Any? = null) = graph.addEdge(DbServiceEdge(db, s, payload))

    fun services() = graph.vertices().filterIsInstance<Service>()

    fun databases() = graph.vertices().filterIsInstance<Database>()

    fun linksDatabasesServices() = graph.edges().filterIsInstance<DbServiceEdge>()
}