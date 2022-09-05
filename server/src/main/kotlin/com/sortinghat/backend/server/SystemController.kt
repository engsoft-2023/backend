package com.sortinghat.backend.server

import com.sortinghat.backend.data_collector.services.RegisterNewSystem
import com.sortinghat.backend.metrics_extractor.services.ExtractSystemMetrics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems")
class SystemController(
    @Autowired private val registerNewSystem: RegisterNewSystem,
    @Autowired private val extractSystemMetrics: ExtractSystemMetrics,
    @Autowired private val systemService: SystemService
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll() = SystemDto.createFromSystems(systemService.findAllSystems())

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable name: String) = SystemDto.createFromServices(systemService.findAllServicesBySystem(name))

    @GetMapping("/{name}/metrics")
    @ResponseStatus(HttpStatus.OK)
    fun getMetrics(@PathVariable name: String) = extractSystemMetrics.execute(name)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registerSystem(@RequestBody request: RegisterSystemDto) = SystemDto.createFromServices(registerNewSystem.execute(request.repoUrl, request.filename))
}
