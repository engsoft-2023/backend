package com.erickrodrigues.staticcollector.application.controllers

import com.erickrodrigues.staticcollector.application.http.requests.RegisterSystemRequest
import com.erickrodrigues.staticcollector.application.http.responses.RegisterSystemResponse
import com.erickrodrigues.staticcollector.domain.exceptions.UnableToFetchDataException
import com.erickrodrigues.staticcollector.domain.exceptions.UnableToParseDataException
import com.erickrodrigues.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.erickrodrigues.staticcollector.domain.usecases.ExtractDataUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems")
class SystemController(private val factory: ExtractionComponentsAbstractFactory) {

    @PostMapping
    fun registerSystem(@RequestBody request: RegisterSystemRequest): ResponseEntity<RegisterSystemResponse> {
        return try {
            val extractDataUseCase = ExtractDataUseCase(factory)
            val system = extractDataUseCase.run(request.repoUrl)
            ResponseEntity(RegisterSystemResponse(system), HttpStatus.OK)
        } catch (e: Exception) {
            handleExceptions(e)
        }
    }

    private fun <T> handleExceptions(e: Exception): ResponseEntity<T> {
        return when (e) {
            is UnableToFetchDataException -> ResponseEntity<T>(null, HttpStatus.NOT_FOUND)
            is UnableToParseDataException -> ResponseEntity<T>(null, HttpStatus.BAD_REQUEST)
            else -> ResponseEntity<T>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}