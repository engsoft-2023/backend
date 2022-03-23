package com.thesortinghat.staticcollector.domain.model

class Graph {
    private val graph: HashMap<String, MutableList<Edge>> = HashMap()
    private val vertices: HashMap<String, Vertex> = HashMap()
    private val edges: HashMap<String, Edge> = HashMap()

    fun addVertex(v: Vertex) {
        if (v.id !in graph) {
            graph[v.id] = mutableListOf()
            vertices[v.id] = v
        }
    }

    fun addEdge(e: Edge) {
        val u = e.first
        val v = e.second
        addVertex(u)
        addVertex(v)
        graph[u.id]!!.add(e)
        graph[v.id]!!.add(e)
        edges[e.id] = e
    }

    fun vertices() = vertices.values.toList()

    fun edges() = edges.values.toList()
}