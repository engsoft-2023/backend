package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.factories.CollectionComponentsFactory
import com.sortinghat.backend.data_collector.payloads.ServicesEndpointsRegistrationPayload
import com.sortinghat.backend.domain.exceptions.EntityNotFoundException
import com.sortinghat.backend.domain.model.Operation
import com.sortinghat.backend.domain.model.ServiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class RegisterServicesEndpointsImpl(
    @Autowired @Qualifier("openApi") factory: CollectionComponentsFactory,
    @Autowired private val repository: ServiceRepository,
) : RegisterServicesEndpoints {
    private val fetcher: DataFetcher
    private val parser: DataParser
    private val converter: ConverterToDomain<Operation>

    init {
        fetcher = factory.createDataFetcher()
        parser = factory.createDataParser()
        converter = factory.createConverterToDomain()
    }

    override fun execute(systemName: String, payload: ServicesEndpointsRegistrationPayload) {
        val (repoUrl, servicesAndOpenApiFilenames) = payload
        val services = repository.findAllBySystem(systemName)

        if (services.isEmpty()) {
            throw EntityNotFoundException("unable to register endpoints: system $systemName not found")
        }

        servicesAndOpenApiFilenames.forEach { (serviceName, openApiFilename) ->
            if (openApiFilename.isEmpty()) return@forEach

            val service = services.find { s -> s.name == serviceName }
                ?: throw EntityNotFoundException("unable to register endpoints for service $serviceName: service not found")

            val response = fetcher.execute(repoUrl, openApiFilename)
            val openApi = parser.execute(response)
            val operations = converter.execute(openApi)

            operations.forEach { operation -> service.expose(operation) }
        }

        repository.saveAll(services)
    }
}
