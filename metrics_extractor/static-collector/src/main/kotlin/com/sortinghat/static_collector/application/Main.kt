package com.sortinghat.static_collector.application

import com.sortinghat.static_collector.application.http.HTTPHandler
import com.sortinghat.static_collector.application.yaml.YAMLParser
import com.sortinghat.static_collector.domain.converters.DockerComposeConverter
import com.sortinghat.static_collector.domain.entities.platform_independent_model.System
import com.sortinghat.static_collector.domain.fetchers.DockerComposeFetch
import com.sortinghat.static_collector.domain.ports.repositories.SystemRepository
import com.sortinghat.static_collector.domain.usecases.ExtractDataUseCase

class MockSystemRepo : SystemRepository {
    override fun save(system: System) {
        println("System saved")
    }

    override fun findByName(name: String): System? {
        return null
    }
}

fun main() {
    val extractDataUseCase = ExtractDataUseCase(
        DockerComposeFetch(HTTPHandler()),
        YAMLParser(),
        DockerComposeConverter(),
        MockSystemRepo()
    )
    val system = extractDataUseCase.run("https://github.com/erickrodrigs/dsvendas")
    println(system)
    println(system.services())
    println(system.databases())
}