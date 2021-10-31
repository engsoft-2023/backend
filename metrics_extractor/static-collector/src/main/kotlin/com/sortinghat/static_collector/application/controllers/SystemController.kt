package com.sortinghat.static_collector.application.controllers

import com.sortinghat.static_collector.application.http.requests.RegisterSystemRequest
import com.sortinghat.static_collector.application.http.responses.RegisterSystemResponse
import com.sortinghat.static_collector.application.http.HTTPHandler
import com.sortinghat.static_collector.application.yaml.YAMLParser
import com.sortinghat.static_collector.domain.converters.DockerComposeConverter
import com.sortinghat.static_collector.domain.entities.platform_independent_model.System
import com.sortinghat.static_collector.domain.fetchers.DockerComposeFetch
import com.sortinghat.static_collector.domain.ports.repositories.SystemRepository
import com.sortinghat.static_collector.domain.usecases.ExtractDataUseCase
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems")
class SystemController {

    @PostMapping("/register")
    fun registerSystem(@RequestBody request: RegisterSystemRequest): RegisterSystemResponse {
        val extractDataUseCase = ExtractDataUseCase(
            DockerComposeFetch(HTTPHandler()),
            YAMLParser(),
            DockerComposeConverter(),
            object : SystemRepository {
                override fun save(system: System) { println("System saved") }
                override fun findByName(name: String): System? = null
            }
        )

        val system = extractDataUseCase.run(request.repoUrl)
        return RegisterSystemResponse(system)
    }
}