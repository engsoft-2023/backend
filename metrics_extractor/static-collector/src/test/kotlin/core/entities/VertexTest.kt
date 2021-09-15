package core.entities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

class VertexTest {

    @Test
    @DisplayName("check if vertex instance has a valid id")
    fun testVertexInstantiationWithCorrectUUID() {
        val vertex = Vertex()
        assertDoesNotThrow { UUID.fromString(vertex.id()) }
    }
}