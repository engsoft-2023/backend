package com.sortinghat.static_collector.application.controllers

import com.sortinghat.static_collector.application.factories.ExtractDataFactory
import com.sortinghat.static_collector.application.http.requests.RegisterSystemRequest
import com.sortinghat.static_collector.application.http.responses.RegisterSystemResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems")
class SystemController(private val extractDataFactory: ExtractDataFactory) {

    @PostMapping
    fun registerSystem(@RequestBody request: RegisterSystemRequest): RegisterSystemResponse {
        val extractDataUseCase = extractDataFactory.create()
        val system = extractDataUseCase.run(request.repoUrl)
        return RegisterSystemResponse(system)
    }
}