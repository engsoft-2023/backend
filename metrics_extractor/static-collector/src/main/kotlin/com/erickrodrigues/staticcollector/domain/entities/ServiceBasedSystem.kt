package com.erickrodrigues.staticcollector.domain.entities

import java.util.UUID

data class ServiceBasedSystem(val name: String) {
    val id = UUID.randomUUID().toString()
    private val graph = Graph()

    fun addService(s: Service) = graph.addVertex(s)

    fun addDatabase(db: Database) = graph.addVertex(db)

    fun bindDatabaseToService(db: Database, s: Service) = graph.addEdge(DbServiceEdge(db, s))

    fun services() = graph.vertices().filterIsInstance<Service>()

    fun databases() = graph.vertices().filterIsInstance<Database>()

    fun linksDatabasesServices() = graph.edges().filterIsInstance<DbServiceEdge>()
}