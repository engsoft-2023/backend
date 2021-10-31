package com.sortinghat.static_collector.domain.fetchers

import com.sortinghat.static_collector.domain.ports.HTTPPort
import com.sortinghat.static_collector.domain.exceptions.UnableToFetchDataException
import com.sortinghat.static_collector.domain.responses.HTTPResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class DockerComposeFetchTest {

    private val httpPort by lazy { mock(HTTPPort::class.java) }

    @Test
    @DisplayName("it returns a valid response when fetch is successful")
    fun testFetchForAValidUrl() {
        `when`(httpPort.get(anyString())).thenReturn(HTTPResponse(200, "Ok!"))
        val fetcher = DockerComposeFetch(httpPort)
        val (systemName, data) = fetcher.run("https://github.com/the-sortinghat/static-collector")
        assertEquals("static-collector", systemName)
        assertEquals("Ok!", data)
    }

    @Test
    @DisplayName("it throws an exception when url is invalid")
    fun testFetchForInvalidUrl() {
        val fetcher = DockerComposeFetch(httpPort)
        assertThrows(UnableToFetchDataException::class.java) { fetcher.run("https://github.com/") }
    }

    @Test
    @DisplayName("it throws an exception when response status is not 200")
    fun testWhenFetchFails() {
        `when`(httpPort.get(anyString())).thenReturn(HTTPResponse(404, "Not Found!"))
        val fetcher = DockerComposeFetch(httpPort)
        assertThrows(UnableToFetchDataException::class.java) {
            fetcher.run("https://github.com/the-sortinghat/static-collector")
        }
    }
}