package com.sortinghat.static_collector.domain.entities.base

class Graph {
    var numberOfVertices = 0
    var numberOfEdges = 0
    var adj: HashMap<String, MutableList<Edge>> = hashMapOf()
    var vertices: MutableList<Vertex> = mutableListOf()

    fun addVertex(v: Vertex) {
        val hasVertex = this.vertices.find { u -> u.id == v.id }
        if (hasVertex == null) {
            this.vertices.add(v)
            this.numberOfVertices += 1
            this.adj[v.id] = mutableListOf()
        }
    }

    fun addEdge(e: Edge) {
        val u = e.either()
        val v = e.other(u)

        this.addVertex(u)
        this.addVertex(v)

        this.adj[u.id]!!.add(e)
        this.adj[v.id]!!.add(e)

        this.numberOfEdges += 1
    }

    fun edges(): List<Edge> {
        val allEdges = mutableListOf<Edge>()
        val filteredEdges = mutableListOf<Edge>()

        this.adj.forEach { (_, listEdges) -> allEdges.addAll(listEdges) }

        allEdges.forEach { edge ->
            val fixedEither = edge.either()
            val fixedOther = edge.other(fixedEither)
            val hasEdge = filteredEdges.find { e ->
                val either = e.either()
                val other = e.other(either)
                fixedEither.id == either.id && fixedOther.id == other.id
            }

            if (hasEdge == null)
                filteredEdges.add(edge)
        }

        return filteredEdges
    }
}
