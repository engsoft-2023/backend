package com.erickrodrigues.staticcollector.domain.usecases

import com.erickrodrigues.staticcollector.domain.converters.ConverterToDomain
import com.erickrodrigues.staticcollector.domain.entities.ServiceBasedSystem
import com.erickrodrigues.staticcollector.domain.exceptions.EntityAlreadyExistsException
import com.erickrodrigues.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.erickrodrigues.staticcollector.domain.fetchers.DataFetcher
import com.erickrodrigues.staticcollector.domain.parsers.DataParser
import com.erickrodrigues.staticcollector.domain.ports.ServiceBasedSystemRepository

class ExtractDataUseCase(factory: ExtractionComponentsAbstractFactory) {
    private val fetcher: DataFetcher
    private val parser: DataParser
    private val converter: ConverterToDomain
    private val repository: ServiceBasedSystemRepository

    init {
        fetcher = factory.createDataFetcher()
        parser = factory.createDataParser()
        converter = factory.createConverterToDomain()
        repository = factory.createServiceBasedSystemRepository()
    }

    fun run(url: String): ServiceBasedSystem {
        val response = fetcher.run(url)
        val specificTechnology = parser.run(response)
        val system = converter.run(specificTechnology)
        persistData(system)
        return system
    }

    private fun persistData(system: ServiceBasedSystem) {
        val sys = repository.findByName(system.name)

        if (sys != null)
            throw EntityAlreadyExistsException("system with that name already exists")

        repository.save(system)
    }
}