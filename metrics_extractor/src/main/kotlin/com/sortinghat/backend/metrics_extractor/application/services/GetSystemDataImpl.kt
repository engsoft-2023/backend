package com.sortinghat.backend.metrics_extractor.application.services

import com.sortinghat.backend.domain.model.ServiceRepository
import com.sortinghat.backend.metrics_extractor.domain.services.ExtractSystemMetrics

@org.springframework.stereotype.Service
class GetSystemDataImpl(private val repository: ServiceRepository) : GetSystemData {

    override fun findAllServicesBySystem(id: String) = repository.findAllBySystem(id)

    override fun findAllSystems() = repository.findAllSystems()

    override fun getMetricsBySystem(id: String) = ExtractSystemMetrics(repository).execute(id)
}
