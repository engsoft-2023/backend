package com.thesortinghat.staticcollector.application.services

import com.thesortinghat.staticcollector.application.exceptions.EntityNotFoundException
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GetSystem(@Autowired private val repo: ServiceBasedSystemRepository) {

    fun execute(id: String) =
        repo.findById(id) ?: throw EntityNotFoundException("system with that id doesn't exist")
}
