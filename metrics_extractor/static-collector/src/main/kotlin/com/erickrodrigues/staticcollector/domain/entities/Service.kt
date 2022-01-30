package com.erickrodrigues.staticcollector.domain.entities

class Service : Vertex {
    val name: String

    constructor(name: String): super() {
        this.name = name
    }

    constructor(id: String, name: String): super(id) {
        this.name = name
    }
}