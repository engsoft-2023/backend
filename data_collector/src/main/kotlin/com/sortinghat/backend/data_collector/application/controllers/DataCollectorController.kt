package com.sortinghat.backend.data_collector.application.controllers

import com.sortinghat.backend.data_collector.application.dto.RegisterSystemDto
import com.sortinghat.backend.data_collector.application.dto.SystemDto
import com.sortinghat.backend.data_collector.application.services.GetSystem
import com.sortinghat.backend.data_collector.application.services.RegisterNewSystem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/collection/systems")
class DataCollectorController(
    @Autowired private val registerNewSystem: RegisterNewSystem,
    @Autowired private val getSystem: GetSystem
) {

    @GetMapping("/{id}")
    fun getSystem(@PathVariable id: String): ResponseEntity<SystemDto> {
        return ResponseEntity(getSystem.execute(id), HttpStatus.OK)
    }

    @PostMapping
    fun registerSystem(@RequestBody request: RegisterSystemDto): ResponseEntity<SystemDto> {
        val system = registerNewSystem.execute(request.repoUrl, request.filename)
        return ResponseEntity(system, HttpStatus.CREATED)
    }
}
