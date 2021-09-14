package core.entities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GraphTest {

    @Test
    @DisplayName("check if addVertex works")
    fun testAddVertex() {
        val graph = Graph()
        val v = Vertex()
        graph.addVertex(v)

        assertEquals(1, graph.amountVertices())
        assertEquals(v, graph.getVertices().find { u -> u.getId() == v.getId() })
    }

    @Test
    @DisplayName("check if addEdge works")
    fun testAddEdge() {
        val graph = Graph()
        val u = Vertex()
        val v = Vertex()
        val e = Edge(u, v)

        graph.addEdge(e)

        assertEquals(1, graph.amountEdges())
        assertEquals(1, graph.getEdges().size)
        assertEquals(e, graph.getEdges()[0])
    }
}