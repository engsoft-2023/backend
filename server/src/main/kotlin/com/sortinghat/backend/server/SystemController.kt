package com.sortinghat.backend.server

import com.sortinghat.backend.data_collector.payloads.ServicesEndpointsRegistrationPayload
import com.sortinghat.backend.data_collector.payloads.ServicesSyncAndAsyncOperationsPayload
import com.sortinghat.backend.data_collector.services.RegisterNewSystem
import com.sortinghat.backend.data_collector.services.RegisterServicesEndpoints
import com.sortinghat.backend.data_collector.services.RegisterSyncAndAsyncOperations
import com.sortinghat.backend.metrics_extractor.services.ExtractSystemMetrics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems")
class SystemController(
    @Autowired private val registerNewSystem: RegisterNewSystem,
    @Autowired private val registerServicesEndpoints: RegisterServicesEndpoints,
    @Autowired private val registerSyncAndAsyncOperations: RegisterSyncAndAsyncOperations,
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

    @PutMapping("/{name}/endpoints")
    @ResponseStatus(HttpStatus.OK)
    fun registerServicesEndpoints(@PathVariable name: String, @RequestBody payload: ServicesEndpointsRegistrationPayload) {
        registerServicesEndpoints.execute(name, payload)
    }

    @PutMapping("/{name}/syncAndAsync")
    @ResponseStatus(HttpStatus.OK)
    fun registerServicesSyncAndAsyncOperations(@PathVariable name: String, @RequestBody payload: ServicesSyncAndAsyncOperationsPayload) {
        registerSyncAndAsyncOperations.execute(name, payload)
    }
}
