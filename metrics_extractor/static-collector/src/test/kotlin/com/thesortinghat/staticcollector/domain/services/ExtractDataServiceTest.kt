package com.thesortinghat.staticcollector.domain.services

import com.thesortinghat.staticcollector.domain.converters.ConverterToDomain
import com.thesortinghat.staticcollector.domain.entities.Database
import com.thesortinghat.staticcollector.domain.entities.Service
import com.thesortinghat.staticcollector.domain.entities.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.entities.SpecificTechnology
import com.thesortinghat.staticcollector.domain.exceptions.EntityAlreadyExistsException
import com.thesortinghat.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.thesortinghat.staticcollector.domain.fetchers.DataFetcher
import com.thesortinghat.staticcollector.domain.vo.FetchResponse
import com.thesortinghat.staticcollector.domain.ports.DataParserPort
import com.thesortinghat.staticcollector.domain.ports.MessageBroker
import com.thesortinghat.staticcollector.domain.ports.ServiceBasedSystemRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import org.mockito.kotlin.any

class ExtractDataServiceTest {

    private lateinit var extractDataService: ExtractDataService
    private val repo by lazy { mock(ServiceBasedSystemRepository::class.java) }
    private val broker by lazy { mock(MessageBroker::class.java) }
    private val system by lazy {
        val service = Service("1", "foo")
        val db = Database("2", "bar", "baz", "baz")
        val system = ServiceBasedSystem("3", "Sorting Hat")
        system.addService(service)
        system.addDatabase(db)
        system.addDatabaseUsage(db, service)
        system
    }

    @BeforeEach
    fun init() {
        val response = FetchResponse("", "")
        val specificTechnology = mock(SpecificTechnology::class.java)
        val factory = mock(ExtractionComponentsAbstractFactory::class.java)
        val fetcher = mock(DataFetcher::class.java)
        val parser = mock(DataParserPort::class.java)
        val converter = mock(ConverterToDomain::class.java)
        `when`(fetcher.run(anyString(), anyString())).thenReturn(response)
        `when`(parser.run(response)).thenReturn(specificTechnology)
        `when`(converter.run(specificTechnology)).thenReturn(system)
        `when`(factory.createDataFetcher()).thenReturn(fetcher)
        `when`(factory.createDataParser()).thenReturn(parser)
        `when`(factory.createConverterToDomain()).thenReturn(converter)
        `when`(factory.createServiceBasedSystemRepository()).thenReturn(repo)
        `when`(factory.createMessageBroker()).thenReturn(broker)
        extractDataService = ExtractDataService(factory)
    }

    @Test
    fun `call extract use case for a system which name already exists will throw an exception`() {
        `when`(repo.findByName(anyString())).thenReturn(ServiceBasedSystem("Sorting Hat"))
        assertThrows(EntityAlreadyExistsException::class.java) {
            extractDataService.run("https://github.com", "docker-compose.yaml")
        }
    }

    @Test
    fun `it works properly for a system which name doesn't exist`() {
        `when`(repo.findByName(anyString())).thenReturn(null)
        extractDataService.run("https://github.com", "docker-compose.yaml")
        verify(repo, times(1)).save(any())
    }

    @Test
    fun `it sends the collected data to the message queue`() {
        `when`(repo.findByName(anyString())).thenReturn(null)
        extractDataService.run("https://github.com", "docker-compose.yaml")
        verify(broker, times(1)).newSystem(any())
        verify(broker, times(1)).newService(any())
        verify(broker, times(1)).newDatabase(any())
        verify(broker, times(1)).newUsage(any())
    }
}
