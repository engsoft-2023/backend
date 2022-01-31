package com.erickrodrigues.staticcollector.domain.entities

import java.util.UUID

abstract class Vertex : GraphComponent {
    val id: String

    constructor() {
        id = UUID.randomUUID().toString()
    }

    constructor(id: String) {
        this.id = id
    }
}