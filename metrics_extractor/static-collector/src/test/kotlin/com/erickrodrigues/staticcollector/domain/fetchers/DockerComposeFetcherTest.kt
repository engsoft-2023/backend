package com.erickrodrigues.staticcollector.domain.fetchers

import com.erickrodrigues.staticcollector.domain.exceptions.UnableToFetchDataException
import com.erickrodrigues.staticcollector.domain.http.HttpPort
import com.erickrodrigues.staticcollector.domain.vo.ResponseHttp
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class DockerComposeFetcherTest {
    private val httpPort by lazy { mock(HttpPort::class.java) }

    @Test
    fun `it returns a valid response when fetch is successful`() {
        `when`(httpPort.get(anyString())).thenReturn(ResponseHttp(200, "Ok!"))
        val fetcher = DockerComposeFetcher(httpPort)
        val (systemName, data) = fetcher.run("https://github.com/the-sortinghat/static-collector")
        assertEquals("static-collector", systemName)
        assertEquals("Ok!", data)
    }

    @Test
    fun `it throws an exception when url is invalid`() {
        val fetcher = DockerComposeFetcher(httpPort)
        assertThrows(UnableToFetchDataException::class.java) { fetcher.run("https://github.com/") }
    }

    @Test
    fun `it throws an exception when response status is not 200`() {
        `when`(httpPort.get(anyString())).thenReturn(ResponseHttp(404, "Not Found!"))
        val fetcher = DockerComposeFetcher(httpPort)
        assertThrows(UnableToFetchDataException::class.java) {
            fetcher.run("https://github.com/the-sortinghat/static-collector")
        }
    }
}