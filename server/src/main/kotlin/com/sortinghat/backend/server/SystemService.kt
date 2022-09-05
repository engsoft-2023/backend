package com.sortinghat.backend.server

import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.domain.model.ServiceBasedSystem

interface SystemService {
    fun findAllServicesBySystem(id: String): Set<Service>

    fun findAllSystems(): Set<ServiceBasedSystem>
}
