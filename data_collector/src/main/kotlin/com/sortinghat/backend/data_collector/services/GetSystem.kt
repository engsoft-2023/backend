package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.dto.SystemDto
import com.sortinghat.backend.data_collector.exceptions.EntityNotFoundException
import com.sortinghat.backend.domain.model.ServiceRepository

@org.springframework.stereotype.Service
class GetSystem(private val repo: ServiceRepository) {

    fun execute(id: String): SystemDto {
        val services = repo.findAllBySystem(id)

        if (services.isEmpty()) {
            throw EntityNotFoundException("system with that id doesn't exist")
        }

        return SystemDto.createFromServices(services)
    }
}
