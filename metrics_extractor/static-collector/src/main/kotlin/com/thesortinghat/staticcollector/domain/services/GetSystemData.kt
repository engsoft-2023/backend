package com.thesortinghat.staticcollector.domain.services

import com.thesortinghat.staticcollector.domain.exceptions.EntityNotFoundException
import com.thesortinghat.staticcollector.domain.ports.ServiceBasedSystemRepository

class GetSystemData(private val repo: ServiceBasedSystemRepository) {

    fun run(id: String) =
        repo.findById(id) ?: throw EntityNotFoundException("system with that id doesn't exist")
}
