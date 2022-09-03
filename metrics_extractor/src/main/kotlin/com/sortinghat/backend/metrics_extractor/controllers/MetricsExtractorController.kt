package com.sortinghat.backend.metrics_extractor.controllers

import com.sortinghat.backend.metrics_extractor.dto.SystemDto
import com.sortinghat.backend.metrics_extractor.services.GetSystemData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems")
class MetricsExtractorController(private val getSystemData: GetSystemData) {

    @GetMapping
    fun getAll() = SystemDto.createFromSystems(getSystemData.findAllSystems())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String) = SystemDto.createFromServices(getSystemData.findAllServicesBySystem(id))

    @GetMapping("/{id}/metrics")
    fun getMetrics(@PathVariable id: String) = getSystemData.getMetricsBySystem(id)
}
