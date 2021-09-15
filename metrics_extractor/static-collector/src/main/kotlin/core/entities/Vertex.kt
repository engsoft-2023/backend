package core.entities

import java.util.UUID.randomUUID

abstract class Vertex {
    protected var id: String = randomUUID().toString()
    fun id() = this.id
}
