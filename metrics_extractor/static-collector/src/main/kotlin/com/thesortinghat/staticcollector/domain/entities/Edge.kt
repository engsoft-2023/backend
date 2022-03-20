package com.thesortinghat.staticcollector.domain.entities

import java.util.UUID

abstract class Edge(val first: Vertex, val second: Vertex) {
    val id: String = UUID.randomUUID().toString()
}