package com.sortinghat.static_collector.application.controllers

import com.sortinghat.static_collector.application.factories.ExtractDataFactory
import com.sortinghat.static_collector.application.http.requests.RegisterSystemRequest
import com.sortinghat.static_collector.application.http.responses.RegisterSystemResponse
import com.sortinghat.static_collector.domain.converters.ConverterToPIM
import com.sortinghat.static_collector.domain.entities.platform_independent_model.System
import com.sortinghat.static_collector.domain.exceptions.UnableToFetchDataException
import com.sortinghat.static_collector.domain.exceptions.UnableToParseDataException
import com.sortinghat.static_collector.domain.fetchers.FetchData
import com.sortinghat.static_collector.domain.fetchers.FetchResponse
import com.sortinghat.static_collector.domain.ports.ParseData
import com.sortinghat.static_collector.domain.ports.repositories.SystemRepository
import com.sortinghat.static_collector.domain.usecases.ExtractDataUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.ResponseEntity

@DisplayName("SystemController")
class SystemControllerTest {

    @Nested
    @DisplayName("POST /systems")
    inner class PostSystems {

        @Nested
        @DisplayName("when request is successful")
        inner class SuccessfulRequest {
            private lateinit var response: ResponseEntity<RegisterSystemResponse>
            private lateinit var system: System

            private fun initSystem() {
                system = System("my-system")
            }

            @BeforeEach
            fun init() {
                initSystem()

                val extractDataUseCase = Mockito.mock(ExtractDataUseCase::class.java)
                val factory = Mockito.mock(ExtractDataFactory::class.java)

                Mockito.`when`(extractDataUseCase.run(Mockito.anyString())).thenReturn(system)
                Mockito.`when`(factory.create()).thenReturn(extractDataUseCase)

                val controller = SystemController(factory)
                val request = RegisterSystemRequest("https://foo.com/bar")
                response = controller.registerSystem(request)
            }

            @Test
            fun `it returns a successful status code`() {
                val status = response.statusCodeValue
                val isSuccessCode = status in 200..299
                assertTrue(isSuccessCode)
            }

            @Test
            fun `it returns a non-null response`() {
                val body = response.body
                assertNotNull(body)
            }

            @Test
            fun `it returns a saved system`() {
                val body = response.body
                assertEquals("my-system", body!!.name)
            }
        }

        @Nested
        @DisplayName("when request fails")
        inner class FailedRequest {
            private lateinit var response: ResponseEntity<RegisterSystemResponse>
            private lateinit var factory: ExtractDataFactory
            private lateinit var fetchData: FetchData
            private lateinit var parseData: ParseData

            @BeforeEach
            fun init() {
                val converter = Mockito.mock(ConverterToPIM::class.java)
                val repo = Mockito.mock(SystemRepository::class.java)
                fetchData = Mockito.mock(FetchData::class.java)
                parseData = Mockito.mock(ParseData::class.java)
                factory = Mockito.mock(ExtractDataFactory::class.java)

                val extractDataUseCase = ExtractDataUseCase(fetchData, parseData, converter, repo)

                Mockito.`when`(factory.create()).thenReturn(extractDataUseCase)
            }

            @Nested
            @DisplayName("when collector does not find the system data")
            inner class NotFound {

                @BeforeEach
                fun init() {
                    Mockito.`when`(fetchData.run(Mockito.anyString()))
                        .thenAnswer { throw UnableToFetchDataException("") }

                    val controller = SystemController(factory)
                    val request = RegisterSystemRequest("https://foo.com/bar")
                    response = controller.registerSystem(request)
                }

                @Test
                fun `it returns a not found status code`() {
                    val status = response.statusCodeValue
                    assertEquals(404, status)
                }
            }

            @Nested
            @DisplayName("when collector cannot parse the system data")
            inner class BadRequestFail {

                @BeforeEach
                fun init() {
                    val fetchResponse = FetchResponse("foo", "bar")

                    Mockito.`when`(fetchData.run(Mockito.anyString()))
                        .thenReturn(fetchResponse)

                    Mockito.`when`(parseData.run(fetchResponse))
                        .thenAnswer { throw UnableToParseDataException("") }

                    val controller = SystemController(factory)
                    val request = RegisterSystemRequest("https://foo.com/bar")
                    response = controller.registerSystem(request)
                }

                @Test
                fun `it returns a bad request status code`() {
                    val status = response.statusCodeValue
                    assertEquals(400, status)
                }
            }
        }
    }
}