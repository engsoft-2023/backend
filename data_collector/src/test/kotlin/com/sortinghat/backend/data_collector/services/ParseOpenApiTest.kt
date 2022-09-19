package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.OpenApi
import com.sortinghat.backend.data_collector.exceptions.UnableToParseDataException
import com.sortinghat.backend.data_collector.utils.FetchResponse
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class ParseOpenApiTest {

    @Nested
    @DisplayName("when parsing is successful")
    inner class SuccessfulParsing {
        private lateinit var openApi: OpenApi

        @BeforeEach
        fun init() {
            val rawOpenApi = """
                openapi: 3.0.0
                info:
                  title: Catalog Service API
                servers:
                  - url: https://catalogservice.com
                paths:
                  /catalog:
                    get:
                      summary: Returns a list of products.
            """.trimIndent()

            val parseOpenApi = ParseOpenApi()
            openApi = parseOpenApi.execute(FetchResponse("my-system", rawOpenApi))
        }

        @Test
        fun `it should have non-null value for all properties`() {
            assertNotNull(openApi.openapi)
            assertNotNull(openApi.info)
            assertNotNull(openApi.servers)
        }

        @Test
        fun `it should return only one path`() {
            assertEquals(1, openApi.paths.size)
        }

        @Test
        fun `it should return only one verb for the unique path`() {
            assertEquals(1, openApi.paths["/catalog"]!!.size)
            assertTrue(openApi.paths["/catalog"]!!.containsKey("get"))
        }
    }

    @Nested
    @DisplayName("when parsing fails")
    inner class ParsingFails {

        @Test
        fun `should throw an exception when there are no paths`() {
            val rawOpenApi = """
                openapi: 3.0.0
                info:
                  title: Catalog Service API
                servers:
                  - url: https://catalogservice.com
            """.trimIndent()

            val parseOpenApi = ParseOpenApi()
            val exception = assertThrows<UnableToParseDataException> {
                parseOpenApi.execute(FetchResponse("my-system", rawOpenApi))
            }
            assertEquals("unable to parse OpenAPI file: OpenAPI file does not have any paths", exception.message)
        }
    }
}
