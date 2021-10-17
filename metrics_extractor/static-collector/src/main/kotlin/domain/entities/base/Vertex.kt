package domain.entities.base

import java.util.UUID.randomUUID

abstract class Vertex {
    val id: String = randomUUID().toString()
}
