package core.entities

class Graph {
    private var v = 0
    private var e = 0
    private var adj: HashMap<String, MutableList<Edge>> = hashMapOf()
    private var vertices: MutableList<Vertex> = mutableListOf()

    fun amountVertices() = this.v

    fun amountEdges() = this.e

    fun getVertices() = this.vertices

    fun addVertex(v: Vertex) {
        val hasVertex = this.vertices.find { u -> u.getId() == v.getId() }
        if (hasVertex == null) {
            this.vertices.add(v)
            this.v += 1
            this.adj[v.getId()] = mutableListOf()
        }
    }

    fun addEdge(e: Edge) {
        val u = e.either()
        val v = e.other(u)

        this.addVertex(u)
        this.addVertex(v)

        this.adj[u.getId()]!!.add(e)
        this.adj[v.getId()]!!.add(e)

        this.e += 1
    }

    fun getEdges(): List<Edge> {
        val allEdges = mutableListOf<Edge>()
        val filteredEdges = mutableListOf<Edge>()

        this.adj.forEach { (_, listEdges) -> allEdges.addAll(listEdges) }

        allEdges.forEach { edge ->
            val fixedEither = edge.either()
            val fixedOther = edge.other(fixedEither)
            val hasEdge = filteredEdges.find { e ->
                val either = e.either()
                val other = e.other(either)
                fixedEither.getId() == either.getId() && fixedOther.getId() == other.getId()
            }

            if (hasEdge == null)
                filteredEdges.add(edge)
        }

        return filteredEdges
    }
}
