package com.thesortinghat.staticcollector.domain.model

data class Service(val name: String) : Vertex() {
    constructor(id: String, name: String): this(name) {
        this.id = id
    }
}
