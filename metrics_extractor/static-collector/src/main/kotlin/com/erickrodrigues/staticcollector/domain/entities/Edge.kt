package com.erickrodrigues.staticcollector.domain.entities

import java.util.UUID

abstract class Edge(val first: Vertex, val second: Vertex): GraphComponent() {
    val id: String = UUID.randomUUID().toString()
}