package com.erickrodrigues.staticcollector.application.factories

import com.erickrodrigues.staticcollector.application.http.HttpAdapter
import com.erickrodrigues.staticcollector.application.yaml.DockerComposeParser
import com.erickrodrigues.staticcollector.domain.converters.DockerComposeToDomain
import com.erickrodrigues.staticcollector.domain.entities.ServiceBasedSystem
import com.erickrodrigues.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.erickrodrigues.staticcollector.domain.fetchers.DockerComposeFetcher
import com.erickrodrigues.staticcollector.domain.ports.ServiceBasedSystemRepository
import org.springframework.stereotype.Component

@Component
class DockerComposeExtractionComponentsFactory : ExtractionComponentsAbstractFactory {
    override fun createDataFetcher() = DockerComposeFetcher(HttpAdapter())

    override fun createDataParser() = DockerComposeParser()

    override fun createConverterToDomain() = DockerComposeToDomain()

    override fun createServiceBasedSystemRepository() = object : ServiceBasedSystemRepository {
        override fun findByName(name: String): ServiceBasedSystem? {
            return null
        }

        override fun save(system: ServiceBasedSystem) {
            println("system saved!")
        }
    }
}