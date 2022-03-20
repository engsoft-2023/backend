package com.thesortinghat.staticcollector.domain.services

import com.thesortinghat.staticcollector.domain.converters.ConverterToDomain
import com.thesortinghat.staticcollector.domain.entities.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.events.NewDatabase
import com.thesortinghat.staticcollector.domain.events.NewService
import com.thesortinghat.staticcollector.domain.events.NewSystem
import com.thesortinghat.staticcollector.domain.events.NewUsage
import com.thesortinghat.staticcollector.domain.exceptions.EntityAlreadyExistsException
import com.thesortinghat.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.thesortinghat.staticcollector.domain.fetchers.DataFetcher
import com.thesortinghat.staticcollector.domain.ports.DataParserPort
import com.thesortinghat.staticcollector.domain.ports.MessageBroker
import com.thesortinghat.staticcollector.domain.ports.ServiceBasedSystemRepository

class ExtractData(factory: ExtractionComponentsAbstractFactory) {
    private val fetcher: DataFetcher
    private val parser: DataParserPort
    private val converter: ConverterToDomain
    private val repository: ServiceBasedSystemRepository
    private val messageBroker: MessageBroker

    init {
        fetcher = factory.createDataFetcher()
        parser = factory.createDataParser()
        converter = factory.createConverterToDomain()
        repository = factory.createServiceBasedSystemRepository()
        messageBroker = factory.createMessageBroker()
    }

    fun run(url: String, filename: String): ServiceBasedSystem {
        val response = fetcher.run(url, filename)
        val specificTechnology = parser.run(response)
        val system = converter.run(specificTechnology)
        persistData(system)
        sendDataToMessageQueue(system)
        return system
    }

    private fun persistData(system: ServiceBasedSystem) {
        val sys = repository.findByName(system.name)

        if (sys != null)
            throw EntityAlreadyExistsException("system with that name already exists")

        repository.save(system)
    }

    private fun sendDataToMessageQueue(system: ServiceBasedSystem) {
        messageBroker.newSystem(NewSystem(system.id, system.name))
        system.services().forEach { messageBroker.newService(NewService(it.id, it.name, system.id)) }
        system.databases().forEach { messageBroker.newDatabase(NewDatabase(it.id, it.make)) }
        system.linksDatabasesServices().forEach { messageBroker.newUsage(NewUsage(it.service.id, it.db.id)) }
    }
}
