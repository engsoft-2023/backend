package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.exceptions.EntityAlreadyExistsException
import com.sortinghat.backend.data_collector.factories.CollectionComponentsFactory
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.domain.model.ServiceBasedSystem
import com.sortinghat.backend.domain.model.ServiceRepository
import org.springframework.beans.factory.annotation.Autowired

@org.springframework.stereotype.Service
class RegisterNewSystemImpl(
    @Autowired factory: CollectionComponentsFactory,
    @Autowired private val repository: ServiceRepository
) : RegisterNewSystem {
    private val fetcher: DataFetcher
    private val parser: DataParser
    private val converter: ConverterToDomain<Service>

    init {
        fetcher = factory.createDataFetcher()
        parser = factory.createDataParser()
        converter = factory.createConverterToDomain()
    }

    override fun execute(url: String, filename: String): Set<Service> {
        val response = fetcher.execute(url, filename)
        val specificTechnology = parser.execute(response)
        val services = converter.execute(specificTechnology)
        persistData(services)
        return services
    }

    private fun persistData(services: Set<Service>) {
        val sys = services
            .fold(setOf<ServiceBasedSystem>()) { systems, service -> systems.plus(service.system) }
            .first()

        val hasSystem = repository.findAllBySystem(sys.name).isNotEmpty()

        if (hasSystem)
            throw EntityAlreadyExistsException("system with that name already exists")

        repository.saveAll(services)
    }
}
