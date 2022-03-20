package com.thesortinghat.staticcollector.domain.entities

import java.util.UUID

abstract class Vertex {
    val id: String

    constructor() {
        id = UUID.randomUUID().toString()
    }

    constructor(id: String) {
        this.id = id
    }
}