package com.sortinghat.backend.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OperationTest {

    @Test
    fun `splits the url into http verb and uri correctly`() {
        val operation = Operation.fromString("GET /users/{id}")
        assertEquals(HttpVerb.GET, operation.verb)
        assertEquals("/users/{id}", operation.uri)
    }

    @Test
    fun `throws an exception when http verb is not valid`() {
        assertFailsWith<IllegalArgumentException> { Operation.fromString("SOME /users/{id}") }
    }

    @Test
    fun `throws an exception when url is not valid`() {
        assertFailsWith<IllegalArgumentException> { Operation.fromString("GETdf000users{id}") }
    }
}
