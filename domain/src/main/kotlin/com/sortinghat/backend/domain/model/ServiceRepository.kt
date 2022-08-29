package com.sortinghat.backend.domain.model

interface ServiceRepository {
    fun findAllBySystem(id: String): Set<Service>

    fun findAllSystems(): Set<ServiceBasedSystem>

    fun saveAll(services: Set<Service>)
}
