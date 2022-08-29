package com.sortinghat.backend.data_collector.domain.services

import com.sortinghat.backend.data_collector.domain.dockercompose.DockerCompose
import com.sortinghat.backend.data_collector.domain.factories.ExtractionComponentsAbstractFactory
import com.sortinghat.backend.data_collector.domain.vo.FetchResponse
import com.sortinghat.backend.domain.model.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ExtractDataTest {

    @Mock
    private lateinit var fetcher: DataFetcher

    @Mock
    private lateinit var parser: DataParser

    @Mock
    private lateinit var converter: ConverterToModel

    @Mock
    private lateinit var factory: ExtractionComponentsAbstractFactory

    private val extractData by lazy { ExtractData(factory) }

    private val services by lazy {
        val db = Database("bar", DataSource.MySql)
        val system = ServiceBasedSystem("Sorting Hat", "")
        val service = Service("foo", "", Module("module"), system)

        service.addUsage(db, DatabaseAccessType.ReadWrite)

        setOf(service)
    }

    @BeforeEach
    fun init() {
        `when`(factory.createDataFetcher()).thenReturn(fetcher)
        `when`(factory.createDataParser()).thenReturn(parser)
        `when`(factory.createConverterToDomain()).thenReturn(converter)
    }

    @Test
    fun `extracts the data successfully`() {
        `when`(fetcher.execute("https://github.com/erickrodrigs/codepix", "docker-compose.yml"))
                .thenReturn(FetchResponse("erickrodrigs/codepix", "some data"))
        `when`(parser.execute(FetchResponse("erickrodrigs/codepix", "some data")))
                .thenReturn(DockerCompose(name = "erickrodrigs/codepix"))
        `when`(converter.execute(DockerCompose(name = "erickrodrigs/codepix"))).thenReturn(services)
        val actual = extractData.execute("https://github.com/erickrodrigs/codepix", "docker-compose.yml")
        assertEquals(1, actual.size)
        assertEquals("foo", actual.first().name)
        assertEquals("Sorting Hat", actual.first().system.name)
    }
}
