package domain.entities.platform_independent_model

import domain.entities.base.Graph
import java.util.*

data class System(val name: String) {
    val id: String = UUID.randomUUID().toString()
    lateinit var graph: Graph
}
