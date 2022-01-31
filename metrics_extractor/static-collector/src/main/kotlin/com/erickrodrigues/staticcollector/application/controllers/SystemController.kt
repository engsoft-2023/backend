package com.erickrodrigues.staticcollector.application.controllers

import com.erickrodrigues.staticcollector.application.http.requests.RegisterSystemRequest
import com.erickrodrigues.staticcollector.application.http.responses.RegisterSystemResponse
import com.erickrodrigues.staticcollector.domain.exceptions.EntityAlreadyExistsException
import com.erickrodrigues.staticcollector.domain.exceptions.UnableToFetchDataException
import com.erickrodrigues.staticcollector.domain.exceptions.UnableToParseDataException
import com.erickrodrigues.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.erickrodrigues.staticcollector.domain.usecases.ExtractDataUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMapAdapter
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems")
class SystemController(private val factory: ExtractionComponentsAbstractFactory) {

    @PostMapping
    fun registerSystem(@RequestBody request: RegisterSystemRequest): ResponseEntity<RegisterSystemResponse> {
        return try {
            val extractDataUseCase = ExtractDataUseCase(factory)
            val system = extractDataUseCase.run(request.repoUrl)
            ResponseEntity(RegisterSystemResponse(system), HttpStatus.CREATED)
        } catch (e: Exception) {
            handleExceptions(e)
        }
    }

    private fun <T> handleExceptions(e: Exception): ResponseEntity<T> {
        val mapError = MultiValueMapAdapter(mapOf("error" to listOf(e.message)))
        return when (e) {
            is UnableToFetchDataException ->ResponseEntity<T>(mapError, HttpStatus.NOT_FOUND)
            is UnableToParseDataException -> ResponseEntity<T>(mapError, HttpStatus.BAD_REQUEST)
            is EntityAlreadyExistsException -> ResponseEntity<T>(mapError, HttpStatus.CONFLICT)
            else -> {
                val internalError = MultiValueMapAdapter(mapOf("error" to listOf("Internal Server Error")))
                ResponseEntity<T>(internalError, HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }
}