package core.entities

abstract class Edge(private val u: Vertex, private val v: Vertex, private val payload: Any? = null) {
    fun either() = this.u
    fun other(w: Vertex): Vertex =
        if (w.id() == this.u.id())
            this.v
        else
            this.u
}