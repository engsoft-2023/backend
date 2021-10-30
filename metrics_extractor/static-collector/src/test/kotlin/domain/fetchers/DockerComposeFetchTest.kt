package domain.fetchers

import domain.ports.HTTPPort
import domain.responses.HTTPResponse
import domain.exceptions.UnableToFetchDataException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class DockerComposeFetchTest {

    @Mock
    private lateinit var httpPort: HTTPPort

    @Test
    @DisplayName("it returns a valid response when fetch is successful")
    fun testFetchForAValidUrl() {
        `when`(httpPort.get(anyString())).thenReturn(HTTPResponse(200, "Ok!"))
        val fetcher = DockerComposeFetch(httpPort)
        val data = fetcher.run("https://github.com/the-sortinghat/static-collector")
        assertEquals("Ok!", data)
    }

    @Test
    @DisplayName("it throws an exception when url is invalid")
    fun testFetchForInvalidUrl() {
        val fetcher = DockerComposeFetch(httpPort)
        assertThrows(UnableToFetchDataException::class.java) { fetcher.run("https://github.com/") }
    }

    @Test
    @DisplayName("it throws an exception when response status is 400")
    fun testWhenFetchFails() {
        `when`(httpPort.get(anyString())).thenReturn(HTTPResponse(404, "Not Found!"))
        val fetcher = DockerComposeFetch(httpPort)
        assertThrows(UnableToFetchDataException::class.java) {
            fetcher.run("https://github.com/the-sortinghat/static-collector")
        }
    }
}