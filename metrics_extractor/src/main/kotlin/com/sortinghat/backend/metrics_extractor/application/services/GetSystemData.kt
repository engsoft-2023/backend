package com.sortinghat.backend.metrics_extractor.application.services

import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.domain.model.ServiceBasedSystem
import com.sortinghat.backend.metrics_extractor.domain.model.Extractions

interface GetSystemData {
    fun findAllServicesBySystem(id: String): Set<Service>
    fun findAllSystems(): Set<ServiceBasedSystem>
    fun getMetricsBySystem(id: String): Extractions
}
