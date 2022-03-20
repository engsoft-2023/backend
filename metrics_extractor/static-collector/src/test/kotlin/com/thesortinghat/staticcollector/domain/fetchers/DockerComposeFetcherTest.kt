package com.thesortinghat.staticcollector.domain.fetchers

import com.thesortinghat.staticcollector.domain.exceptions.UnableToFetchDataException
import com.thesortinghat.staticcollector.domain.ports.HttpPort
import com.thesortinghat.staticcollector.domain.vo.ResponseHttp
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class DockerComposeFetcherTest {
    private val httpPort by lazy { mock(HttpPort::class.java) }

    @Test
    fun `it returns a valid response when fetch from github is successful`() {
        `when`(httpPort.get("https://raw.githubusercontent.com/the-sortinghat/static-collector/main/dev-compose.yaml"))
            .thenReturn(ResponseHttp(200, "Ok!"))
        val fetcher = DockerComposeFetcher(httpPort)
        val (systemName, data) = fetcher.run("https://github.com/the-sortinghat/static-collector", "dev-compose.yaml")
        assertEquals("the-sortinghat/static-collector", systemName)
        assertEquals("Ok!", data)
    }

    @Test
    fun `it returns a valid response when fetch from gitlab is successful`() {
        `when`(httpPort.get("https://gitlab.com/uspcodelab/hubuspinovacao/raw/master/docker-compose.yml"))
            .thenReturn(ResponseHttp(200, "Ok!"))
        val fetcher = DockerComposeFetcher(httpPort)
        val (systemName, data) = fetcher.run("https://gitlab.com/uspcodelab/hubuspinovacao", "docker-compose.yml")
        assertEquals("uspcodelab/hubuspinovacao", systemName)
        assertEquals("Ok!", data)
    }

    @Test
    fun `it throws an exception when url is invalid`() {
        val fetcher = DockerComposeFetcher(httpPort)
        assertThrows(UnableToFetchDataException::class.java) { fetcher.run("https://bitbucket.com/", "prod-compose.yaml") }
    }

    @Test
    fun `it throws an exception when response status is not 200`() {
        `when`(httpPort.get(anyString())).thenReturn(ResponseHttp(404, "Not Found!"))
        val fetcher = DockerComposeFetcher(httpPort)
        assertThrows(UnableToFetchDataException::class.java) {
            fetcher.run("https://github.com/the-sortinghat/static-collector", "dev-compose.yaml")
        }
    }
}
