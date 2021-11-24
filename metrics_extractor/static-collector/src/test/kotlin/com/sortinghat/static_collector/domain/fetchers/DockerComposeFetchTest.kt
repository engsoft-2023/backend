package com.sortinghat.static_collector.domain.fetchers

import com.sortinghat.static_collector.domain.ports.HTTPPort
import com.sortinghat.static_collector.domain.exceptions.UnableToFetchDataException
import com.sortinghat.static_collector.domain.responses.HTTPResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class DockerComposeFetchTest {

    private val httpPort by lazy { mock(HTTPPort::class.java) }

    @Test
    fun `it returns a valid response when fetch is successful`() {
        `when`(httpPort.get(anyString())).thenReturn(HTTPResponse(200, "Ok!"))
        val fetcher = DockerComposeFetch(httpPort)
        val (systemName, data) = fetcher.run("https://github.com/the-sortinghat/static-collector")
        assertEquals("static-collector", systemName)
        assertEquals("Ok!", data)
    }

    @Test
    fun `it throws an exception when url is invalid`() {
        val fetcher = DockerComposeFetch(httpPort)
        assertThrows(UnableToFetchDataException::class.java) { fetcher.run("https://github.com/") }
    }

    @Test
    fun `it throws an exception when response status is not 200`() {
        `when`(httpPort.get(anyString())).thenReturn(HTTPResponse(404, "Not Found!"))
        val fetcher = DockerComposeFetch(httpPort)
        assertThrows(UnableToFetchDataException::class.java) {
            fetcher.run("https://github.com/the-sortinghat/static-collector")
        }
    }
}