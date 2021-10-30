package application

import application.http.HTTPHandler
import application.yaml.YAMLParser
import domain.converters.DockerComposeConverter
import domain.entities.platform_independent_model.System
import domain.fetchers.DockerComposeFetch
import domain.ports.repositories.SystemRepository
import domain.usecases.ExtractDataUseCase

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