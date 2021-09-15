package core.entities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

class SystemTest {
    private lateinit var system: System

    @BeforeEach
    fun init() {
        system = System("Sorting Hat")
        val graph = Graph()
        system.setGraph(graph)
    }

    @Test
    @DisplayName("check if system has a valid id")
    fun testSystemId() {
        assertDoesNotThrow { UUID.fromString(system.getId()) }
    }

    @Test
    @DisplayName("add new context, service and database in a system")
    fun testAddContextServiceAndDatabaseVertices() {
        val graph = system.getGraph()
        val context = Context("data-collector")
        val service = Service("data-collector")
        val database = Database("data-collector-db", "MongoDB", "NoSQL")

        graph.addVertex(context)
        graph.addVertex(service)
        graph.addVertex(database)

        assertEquals(3, graph.amountVertices())
        assertEquals(context, graph.getVertices().find { u -> u.id() == context.id() })
        assertEquals(service, graph.getVertices().find { u -> u.id() == service.id() })
        assertEquals(database, graph.getVertices().find { u -> u.id() == database.id() })
    }

    @Test
    @DisplayName("add CtxServiceEdge, CtxDbEdge and DbServiceEdge in a system")
    fun testAddContextServiceAndDatabaseEdges() {
        val graph = system.getGraph()
        val context = Context("data-collector")
        val service = Service("data-collector")
        val database = Database("data-collector-db", "MongoDB", "NoSQL")

        val ctxDbEdge = CtxDbEdge(context, database)
        val ctxServiceEdge = CtxServiceEdge(context, service)
        val dbServiceEdge = DbServiceEdge(database, service, "namespace" to "data-collector-db")

        graph.addEdge(ctxDbEdge)
        graph.addEdge(ctxServiceEdge)
        graph.addEdge(dbServiceEdge)

        assertEquals(3, graph.amountEdges())
        assertEquals(3, graph.getEdges().size)
    }
}