package domain.entities.platform_independent_model

import java.util.*

data class System(val name: String) {
    val id: String = UUID.randomUUID().toString()
    lateinit var graph: Graph
}
