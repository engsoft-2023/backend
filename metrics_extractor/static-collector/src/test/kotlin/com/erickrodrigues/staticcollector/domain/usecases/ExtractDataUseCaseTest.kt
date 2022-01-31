package com.erickrodrigues.staticcollector.domain.usecases

import com.erickrodrigues.staticcollector.domain.converters.ConverterToDomain
import com.erickrodrigues.staticcollector.domain.entities.Database
import com.erickrodrigues.staticcollector.domain.entities.Service
import com.erickrodrigues.staticcollector.domain.entities.ServiceBasedSystem
import com.erickrodrigues.staticcollector.domain.entities.SpecificTechnology
import com.erickrodrigues.staticcollector.domain.events.NewDatabase
import com.erickrodrigues.staticcollector.domain.events.NewService
import com.erickrodrigues.staticcollector.domain.events.NewSystem
import com.erickrodrigues.staticcollector.domain.events.NewUsage
import com.erickrodrigues.staticcollector.domain.exceptions.EntityAlreadyExistsException
import com.erickrodrigues.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.erickrodrigues.staticcollector.domain.fetchers.DataFetcher
import com.erickrodrigues.staticcollector.domain.fetchers.FetchResponse
import com.erickrodrigues.staticcollector.domain.parsers.DataParser
import com.erickrodrigues.staticcollector.domain.ports.MessageBroker
import com.erickrodrigues.staticcollector.domain.ports.ServiceBasedSystemRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*

class ExtractDataUseCaseTest {

    private lateinit var extractDataUseCase: ExtractDataUseCase
    private val repo by lazy { mock(ServiceBasedSystemRepository::class.java) }
    private val broker by lazy { mock(MessageBroker::class.java) }
    private val system by lazy {
        val service = Service("1", "foo")
        val db = Database("2", "bar", "baz", "baz")
        val system = ServiceBasedSystem("3", "Sorting Hat")
        system.addService(service)
        system.addDatabase(db)
        system
    }

    @BeforeEach
    fun init() {
        val response = FetchResponse("", "")
        val specificTechnology = mock(SpecificTechnology::class.java)
        val factory = mock(ExtractionComponentsAbstractFactory::class.java)
        val fetcher = mock(DataFetcher::class.java)
        val parser = mock(DataParser::class.java)
        val converter = mock(ConverterToDomain::class.java)
        `when`(fetcher.run(anyString())).thenReturn(response)
        `when`(parser.run(response)).thenReturn(specificTechnology)
        `when`(converter.run(specificTechnology)).thenReturn(system)
        `when`(factory.createDataFetcher()).thenReturn(fetcher)
        `when`(factory.createDataParser()).thenReturn(parser)
        `when`(factory.createConverterToDomain()).thenReturn(converter)
        `when`(factory.createServiceBasedSystemRepository()).thenReturn(repo)
        `when`(factory.createMessageBroker()).thenReturn(broker)
        extractDataUseCase = ExtractDataUseCase(factory)
    }

    @Test
    fun `call extract use case for a system which name already exists will throw an exception`() {
        `when`(repo.findByName(anyString())).thenReturn(ServiceBasedSystem("Sorting Hat"))
        assertThrows(EntityAlreadyExistsException::class.java) { extractDataUseCase.run("https://github.com") }
    }

    @Test
    fun `it works properly for a system which name doesn't exist`() {
        `when`(repo.findByName(anyString())).thenReturn(null)
        val s = extractDataUseCase.run("https://github.com")
        verify(repo, times(1)).save(system)
        assertEquals(system, s)
    }

    @Test
    fun `it sends the collected data to the message queue`() {
        `when`(repo.findByName(anyString())).thenReturn(null)
        extractDataUseCase.run("https://github.com")
        verify(broker, times(1)).newSystem(NewSystem(system.id, system.name))
        verify(broker, times(1)).newService(NewService("1", "foo", "3"))
        verify(broker, times(1)).newDatabase(NewDatabase("2", "baz"))
        verify(broker, times(0)).newUsage(NewUsage("", ""))
    }
}