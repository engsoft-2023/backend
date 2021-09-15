package core.entities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class EdgeTest {

    @Test
    @DisplayName("check if either returns the first vertex")
    fun testEither() {
        val u = Vertex()
        val v = Vertex()
        val edge = Edge(u, v)
        assertEquals(u.id(), edge.either().id())
    }

    @Test
    @DisplayName("check if other returns the opposite vertex of the edge")
    fun testOther() {
        val u = Vertex()
        val v = Vertex()
        val edge = Edge(u, v)
        assertEquals(u.id(), edge.other(v).id())
        assertEquals(v.id(), edge.other(u).id())
    }
}