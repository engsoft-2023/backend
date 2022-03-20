package com.thesortinghat.staticcollector.domain.entities

class Database : Vertex {
    val name: String
    val make: String
    val model: String

    constructor(name: String, make: String, model: String): super() {
        this.name = name
        this.make = make
        this.model = model
    }

    constructor(id: String, name: String, make: String, model: String): super(id) {
        this.name = name
        this.make = make
        this.model = model
    }
}