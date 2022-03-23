package com.thesortinghat.staticcollector.domain.model

data class Database(val name: String, val make: String, val model: String) : Vertex() {
    constructor(id: String, name: String, make: String, model: String): this(name, make, model) {
        this.id = id
    }
}
