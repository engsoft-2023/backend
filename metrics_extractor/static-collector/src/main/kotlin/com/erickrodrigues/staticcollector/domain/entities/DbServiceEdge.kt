package com.erickrodrigues.staticcollector.domain.entities

data class DbServiceEdge(
    val db: Database,
    val service: Service,
    val value: Any? = null
) : Edge(db, service, value)