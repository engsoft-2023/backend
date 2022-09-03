package com.sortinghat.backend.data_collector.controllers

import com.sortinghat.backend.data_collector.exceptions.EntityAlreadyExistsException
import com.sortinghat.backend.data_collector.exceptions.EntityNotFoundException
import com.sortinghat.backend.data_collector.exceptions.UnableToFetchDataException
import com.sortinghat.backend.data_collector.exceptions.UnableToParseDataException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class DataCollectorControllerExceptionHandler {

    @ExceptionHandler(value = [UnableToFetchDataException::class])
    fun exception(e: UnableToFetchDataException) =
            ResponseEntity<Any>(mapOf("error" to e.message), HttpStatus.NOT_FOUND)

    @ExceptionHandler(value = [UnableToParseDataException::class])
    fun exception(e: UnableToParseDataException) =
            ResponseEntity<Any>(mapOf("error" to e.message), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(value = [EntityAlreadyExistsException::class])
    fun exception(e: EntityAlreadyExistsException) =
            ResponseEntity<Any>(mapOf("error" to e.message), HttpStatus.CONFLICT)

    @ExceptionHandler(value = [EntityNotFoundException::class])
    fun exception(e: EntityNotFoundException) =
            ResponseEntity<Any>(mapOf("error" to e.message), HttpStatus.NOT_FOUND)
}
