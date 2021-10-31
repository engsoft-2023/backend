package com.sortinghat.static_collector.application.factories

import com.sortinghat.static_collector.application.http.HTTPHandler
import com.sortinghat.static_collector.application.yaml.YAMLParser
import com.sortinghat.static_collector.domain.converters.DockerComposeConverter
import com.sortinghat.static_collector.domain.entities.platform_independent_model.System
import com.sortinghat.static_collector.domain.fetchers.DockerComposeFetch
import com.sortinghat.static_collector.domain.ports.repositories.SystemRepository
import com.sortinghat.static_collector.domain.usecases.ExtractDataUseCase
import org.springframework.stereotype.Component

@Component
class ExtractDockerComposeDataFactory : ExtractDataFactory {
    override fun create() = ExtractDataUseCase(
        DockerComposeFetch(HTTPHandler()),
        YAMLParser(),
        DockerComposeConverter(),
        object : SystemRepository {
            override fun save(system: System) { println("System saved") }
            override fun findByName(name: String): System? = null
        }
    )
}