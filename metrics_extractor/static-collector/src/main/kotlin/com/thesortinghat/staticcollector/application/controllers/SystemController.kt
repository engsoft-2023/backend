package com.thesortinghat.staticcollector.application.controllers

import com.thesortinghat.staticcollector.application.dto.RegisterSystemDto
import com.thesortinghat.staticcollector.application.services.GetSystem
import com.thesortinghat.staticcollector.application.services.RegisterNewSystem
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems")
class SystemController(
        @Autowired private val registerNewSystem: RegisterNewSystem,
        @Autowired private val getSystem: GetSystem
) {

    @GetMapping("/{id}")
    fun getSystem(@PathVariable id: String): ResponseEntity<ServiceBasedSystem> {
        return ResponseEntity(getSystem.execute(id), HttpStatus.OK)
    }

    @PostMapping
    fun registerSystem(@RequestBody request: RegisterSystemDto): ResponseEntity<ServiceBasedSystem> {
        val system = registerNewSystem.execute(request.repoUrl, request.filename)
        return ResponseEntity(system, HttpStatus.CREATED)
    }
}
