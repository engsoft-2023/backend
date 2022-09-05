package com.sortinghat.backend.domain.model

import com.sortinghat.backend.domain.exceptions.EntityNotFoundException
import kotlin.jvm.Throws

interface ServiceRepository {

    @Throws(EntityNotFoundException::class)
    fun findAllBySystem(id: String): Set<Service>

    fun findAllSystems(): Set<ServiceBasedSystem>

    fun saveAll(services: Set<Service>)
}
