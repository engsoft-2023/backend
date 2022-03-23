package com.thesortinghat.staticcollector.domain.services

import com.thesortinghat.staticcollector.domain.dockercompose.DockerCompose
import com.thesortinghat.staticcollector.domain.exceptions.UnableToFetchDataException
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.thesortinghat.staticcollector.domain.vo.FetchResponse
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

    private val system by lazy { ServiceBasedSystem("1", "Sorting Hat") }

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
        `when`(converter.execute(DockerCompose(name = "erickrodrigs/codepix"))).thenReturn(system)
        val actual = extractData.execute("https://github.com/erickrodrigs/codepix", "docker-compose.yml")
        assertEquals(actual.id, system.id)
        assertEquals(actual.name, system.name)
    }
}
