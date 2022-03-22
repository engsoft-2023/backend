package com.thesortinghat.staticcollector.application.controllers

import com.thesortinghat.staticcollector.application.dto.RegisterSystemDto
import com.thesortinghat.staticcollector.application.dto.SystemDto
import com.thesortinghat.staticcollector.domain.services.ExtractDataService
import com.thesortinghat.staticcollector.domain.services.GetSystemService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems")
class SystemController(
        private val extractDataService: ExtractDataService,
        private val getSystemService: GetSystemService
) {

    @GetMapping("/{id}")
    fun getSystem(@PathVariable id: String): ResponseEntity<SystemDto> {
        val system = getSystemService.run(id)
        return ResponseEntity(SystemDto(system), HttpStatus.OK)
    }

    @PostMapping
    fun registerSystem(@RequestBody request: RegisterSystemDto): ResponseEntity<SystemDto> {
        val system = extractDataService.run(request.repoUrl, request.filename)
        return ResponseEntity(SystemDto(system), HttpStatus.CREATED)
    }
}
