package com.sortinghat.static_collector.domain.entities.base

import java.util.UUID.randomUUID

abstract class Vertex {
    val id: String = randomUUID().toString()
}
