package com.sortinghat.backend.data_collector.application.services

import com.sortinghat.backend.data_collector.application.dto.SystemDto
import com.sortinghat.backend.data_collector.application.exceptions.EntityNotFoundException
import com.sortinghat.backend.domain.model.ServiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier

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
