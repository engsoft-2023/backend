package domain.entities.platform_independent_model

import java.util.UUID.randomUUID

abstract class Vertex {
    val id: String = randomUUID().toString()
}
