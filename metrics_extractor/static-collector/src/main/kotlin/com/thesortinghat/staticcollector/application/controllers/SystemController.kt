package com.thesortinghat.staticcollector.application.controllers

import com.thesortinghat.staticcollector.application.factories.GetSystemUseCaseFactory
import com.thesortinghat.staticcollector.application.dto.RegisterSystemDto
import com.thesortinghat.staticcollector.application.dto.SystemDto
import com.thesortinghat.staticcollector.domain.exceptions.EntityAlreadyExistsException
import com.thesortinghat.staticcollector.domain.exceptions.EntityNotFoundException
import com.thesortinghat.staticcollector.domain.exceptions.UnableToFetchDataException
import com.thesortinghat.staticcollector.domain.exceptions.UnableToParseDataException
import com.thesortinghat.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.thesortinghat.staticcollector.domain.services.ExtractData
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMapAdapter
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems")
class SystemController(
    private val extractionFactory: ExtractionComponentsAbstractFactory,
    private val getSystemUseCaseFactory: GetSystemUseCaseFactory
) {

    @GetMapping("/{id}")
    fun getSystem(@PathVariable id: String) =
        try {
            val getSystemUseCase = getSystemUseCaseFactory.create()
            val system = getSystemUseCase.run(id)
            ResponseEntity(SystemDto(system), HttpStatus.OK)
        } catch (e: Exception) {
            handleExceptions(e)
        }

    @PostMapping
    fun registerSystem(@RequestBody request: RegisterSystemDto) =
        try {
            val extractData = ExtractData(extractionFactory)
            val system = extractData.run(request.repoUrl, request.filename)
            ResponseEntity(SystemDto(system), HttpStatus.CREATED)
        } catch (e: Exception) {
            handleExceptions(e)
        }

    private fun <T> handleExceptions(e: Exception): ResponseEntity<T> {
        val mapError = MultiValueMapAdapter(mapOf("error" to listOf(e.message)))
        return when (e) {
            is UnableToFetchDataException -> ResponseEntity<T>(mapError, HttpStatus.NOT_FOUND)
            is UnableToParseDataException -> ResponseEntity<T>(mapError, HttpStatus.BAD_REQUEST)
            is EntityAlreadyExistsException -> ResponseEntity<T>(mapError, HttpStatus.CONFLICT)
            is EntityNotFoundException -> ResponseEntity<T>(mapError, HttpStatus.NOT_FOUND)
            else -> {
                val internalError = MultiValueMapAdapter(mapOf("error" to listOf("Internal Server Error")))
                ResponseEntity<T>(internalError, HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }
}
