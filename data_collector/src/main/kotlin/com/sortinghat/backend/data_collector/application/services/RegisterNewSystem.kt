package com.sortinghat.backend.data_collector.application.services

import com.sortinghat.backend.data_collector.application.dto.SystemDto
import com.sortinghat.backend.data_collector.application.exceptions.EntityAlreadyExistsException
import com.sortinghat.backend.data_collector.domain.services.ExtractData
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.domain.model.ServiceBasedSystem
import com.sortinghat.backend.domain.model.ServiceRepository
import org.springframework.beans.factory.annotation.Autowired

@org.springframework.stereotype.Service
class RegisterNewSystem(
    @Autowired private val extractData: ExtractData,
    @Autowired private val repository: ServiceRepository
) {
    fun execute(url: String, filename: String): SystemDto {
        val services = extractData.execute(url, filename)
        persistData(services)
        return SystemDto.createFromServices(services)
    }

    private fun persistData(services: Set<Service>) {
        val sys = services
            .fold(setOf<ServiceBasedSystem>()) { systems, service -> systems.plus(service.system) }
            .first()

        val hasSystem = repository.findAllBySystem(sys.name).isNotEmpty()

        if (hasSystem)
            throw EntityAlreadyExistsException("system with that name already exists")

        repository.saveAll(services)
    }
}
