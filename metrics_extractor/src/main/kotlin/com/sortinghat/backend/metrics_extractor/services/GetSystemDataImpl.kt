package com.sortinghat.backend.metrics_extractor.services

import com.sortinghat.backend.domain.model.ServiceRepository
import com.sortinghat.backend.metrics_extractor.exceptions.EntityNotFoundException
import com.sortinghat.backend.metrics_extractor.vo.Extractions
import org.springframework.stereotype.Service

@Service
class GetSystemDataImpl(private val repository: ServiceRepository) : GetSystemData {

    override fun findAllServicesBySystem(id: String) = repository.findAllBySystem(id)

    override fun findAllSystems() = repository.findAllSystems()

    override fun getMetricsBySystem(id: String): Extractions {
        val services = repository.findAllBySystem(id)

        if (services.isEmpty()) throw EntityNotFoundException("system with that name not found")

        return ExtractSystemMetrics(services).execute()
    }
}
