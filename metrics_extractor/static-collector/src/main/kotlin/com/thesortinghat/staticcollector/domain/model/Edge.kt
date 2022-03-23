package com.thesortinghat.staticcollector.domain.model

import java.util.UUID

abstract class Edge(val first: Vertex, val second: Vertex) {
    val id: String = UUID.randomUUID().toString()
}