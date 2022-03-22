package com.thesortinghat.staticcollector.domain.entities

data class DatabaseUsage(
    val db: Database,
    val service: Service,
    val payload: Any? = null
) : Edge(db, service)