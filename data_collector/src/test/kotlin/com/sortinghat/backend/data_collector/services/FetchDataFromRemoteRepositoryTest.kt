package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.exceptions.UnableToFetchDataException
import com.sortinghat.backend.data_collector.utils.HttpAbstraction
import com.sortinghat.backend.data_collector.utils.ResponseHttp
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class FetchDataFromRemoteRepositoryTest {

    @Mock
    private lateinit var http: HttpAbstraction<String>

    @Test
    fun `it returns a valid response when fetch from github is successful`() {
        `when`(http.get("https://raw.githubusercontent.com/the-sortinghat/static-collector/main/dev-compose.yaml"))
            .thenReturn(ResponseHttp(200, "Ok!"))
        val fetcher = FetchDataFromRemoteRepository(http)
        val (systemName, data) = fetcher.execute("https://github.com/the-sortinghat/static-collector", "dev-compose.yaml")
        assertEquals("static-collector", systemName)
        assertEquals("Ok!", data)
    }

    @Test
    fun `it returns a valid response when fetch from gitlab is successful`() {
        `when`(http.get("https://gitlab.com/uspcodelab/hubuspinovacao/raw/master/docker-compose.yml"))
            .thenReturn(ResponseHttp(200, "Ok!"))
        val fetcher = FetchDataFromRemoteRepository(http)
        val (systemName, data) = fetcher.execute("https://gitlab.com/uspcodelab/hubuspinovacao", "docker-compose.yml")
        assertEquals("hubuspinovacao", systemName)
        assertEquals("Ok!", data)
    }

    @Test
    fun `it throws an exception when url is invalid`() {
        val fetcher = FetchDataFromRemoteRepository(http)
        assertThrows(UnableToFetchDataException::class.java) { fetcher.execute("https://bitbucket.com/", "prod-compose.yaml") }
    }

    @Test
    fun `it throws an exception when response status is not 200`() {
        `when`(http.get(anyString())).thenReturn(ResponseHttp(404, "Not Found!"))
        val fetcher = FetchDataFromRemoteRepository(http)
        assertThrows(UnableToFetchDataException::class.java) {
            fetcher.execute("https://github.com/the-sortinghat/static-collector", "dev-compose.yaml")
        }
    }
}
