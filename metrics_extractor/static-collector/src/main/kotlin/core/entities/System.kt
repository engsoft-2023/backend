package core.entities

import java.util.*

data class System(val name: String) {
    private var id: String = UUID.randomUUID().toString()
    private lateinit var graph: Graph

    fun getId() = id

    fun getGraph() = graph

    fun setGraph(g: Graph) {
        graph = g
    }
}
