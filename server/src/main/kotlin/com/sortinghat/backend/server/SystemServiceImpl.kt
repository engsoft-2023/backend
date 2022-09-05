package com.sortinghat.backend.server

import com.sortinghat.backend.domain.exceptions.EntityNotFoundException
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.domain.model.ServiceRepository
import org.springframework.beans.factory.annotation.Autowired

@org.springframework.stereotype.Service
class SystemServiceImpl(
    @Autowired private val repository: ServiceRepository
) : SystemService {
    override fun findAllServicesBySystem(id: String): Set<Service> {
        val services = repository.findAllBySystem(id)

        if (services.isEmpty()) throw EntityNotFoundException("system with that name does not exist")

        return services
    }

    override fun findAllSystems() = repository.findAllSystems()
}
