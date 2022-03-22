package com.thesortinghat.staticcollector.application.controllers

import com.thesortinghat.staticcollector.domain.exceptions.EntityAlreadyExistsException
import com.thesortinghat.staticcollector.domain.exceptions.EntityNotFoundException
import com.thesortinghat.staticcollector.domain.exceptions.UnableToFetchDataException
import com.thesortinghat.staticcollector.domain.exceptions.UnableToParseDataException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class SystemControllerExceptionHandler {

    @ExceptionHandler(value = [UnableToFetchDataException::class])
    fun exception(e: UnableToFetchDataException) =
            ResponseEntity<Any>(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(value = [UnableToParseDataException::class])
    fun exception(e: UnableToParseDataException) =
            ResponseEntity<Any>(e.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(value = [EntityAlreadyExistsException::class])
    fun exception(e: EntityAlreadyExistsException) =
            ResponseEntity<Any>(e.message, HttpStatus.CONFLICT)

    @ExceptionHandler(value = [EntityNotFoundException::class])
    fun exception(e: EntityNotFoundException) =
            ResponseEntity<Any>(e.message, HttpStatus.NOT_FOUND)
}
