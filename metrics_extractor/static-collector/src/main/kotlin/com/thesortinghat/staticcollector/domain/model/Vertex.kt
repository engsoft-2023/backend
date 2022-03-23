package com.thesortinghat.staticcollector.domain.model

import java.util.UUID

abstract class Vertex {
    var id: String
        protected set

    init {
        id = UUID.randomUUID().toString()
    }
}
