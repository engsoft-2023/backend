package core.entities

import java.util.UUID.randomUUID

open class Vertex {
    protected var id: String = randomUUID().toString()
    fun id() = this.id
}
