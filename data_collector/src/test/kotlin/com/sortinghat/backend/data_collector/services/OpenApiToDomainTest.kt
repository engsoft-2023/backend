package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.OpenApi
import com.sortinghat.backend.data_collector.domain.SpecificTechnology
import com.sortinghat.backend.data_collector.exceptions.UnableToConvertDataException
import com.sortinghat.backend.domain.model.HttpVerb
import com.sortinghat.backend.domain.model.Operation
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock

class OpenApiToDomainTest {
    private val openApi = OpenApi(
        paths = mapOf(
            "/users" to mapOf(
                "get" to Any(),
                "post" to Any(),
            ),
            "/users/{userId}" to mapOf(
                "get" to Any(),
                "put" to Any(),
                "delete" to Any(),
            ),
        )
    )

    @Test
    fun `it should return a valid set of operations`() {
        val openApiToDomain = OpenApiToDomain()
        val operations = openApiToDomain.execute(openApi)
        val expected = setOf(
            Operation(HttpVerb.GET, "/users"),
            Operation(HttpVerb.POST, "/users"),
            Operation(HttpVerb.GET, "/users/{userId}"),
            Operation(HttpVerb.PUT, "/users/{userId}"),
            Operation(HttpVerb.DELETE, "/users/{userId}")
        )

        assertEquals(expected, operations)
    }

    @Test
    fun `it should throw an exception when converting fails`() {
        val specificTechnology = mock(SpecificTechnology::class.java)
        assertThrows<UnableToConvertDataException> { OpenApiToDomain().execute(specificTechnology) }
    }
}
