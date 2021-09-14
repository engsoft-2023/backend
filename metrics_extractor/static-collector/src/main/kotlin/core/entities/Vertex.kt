package core.entities

import java.util.UUID.randomUUID

class Vertex {
    private val id: String = randomUUID().toString()

    fun getId() = this.id
}