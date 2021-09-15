package core.entities

open class Edge(private val u: Vertex, private val v: Vertex, val payload: Any? = null) {
    fun either() = this.u
    fun other(v: Vertex): Vertex =
        if (v.id() == this.u.id())
            this.v
        else
            this.u
}