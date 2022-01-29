package com.erickrodrigues.staticcollector.domain.entities

import java.util.UUID

abstract class Vertex {
    val id: String = UUID.randomUUID().toString()
}