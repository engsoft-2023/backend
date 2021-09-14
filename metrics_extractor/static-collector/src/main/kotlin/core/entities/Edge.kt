package core.entities

class Edge(private val u: Vertex, private val v: Vertex, val payload: Any? = null) {
    fun either() = this.u
    fun other(v: Vertex): Vertex =
        if (v.getId() == this.u.getId())
            this.v
        else
            this.u
}