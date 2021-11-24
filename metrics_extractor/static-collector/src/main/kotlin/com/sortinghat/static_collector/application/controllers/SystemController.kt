package com.sortinghat.static_collector.application.controllers

import com.sortinghat.static_collector.application.factories.ExtractDataFactory
import com.sortinghat.static_collector.application.http.requests.RegisterSystemRequest
import com.sortinghat.static_collector.application.http.responses.RegisterSystemResponse
import com.sortinghat.static_collector.domain.exceptions.UnableToFetchDataException
import com.sortinghat.static_collector.domain.exceptions.UnableToParseDataException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems")
class SystemController(private val extractDataFactory: ExtractDataFactory) {

    @PostMapping
    fun registerSystem(@RequestBody request: RegisterSystemRequest): ResponseEntity<RegisterSystemResponse> {
        return try {
            val extractDataUseCase = extractDataFactory.create()
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