package com.thesortinghat.staticcollector.domain.ports

import com.thesortinghat.staticcollector.domain.entities.ServiceBasedSystem

interface ServiceBasedSystemRepository {
    fun findById(id: String): ServiceBasedSystem?
    fun findByName(name: String): ServiceBasedSystem?
    fun save(system: ServiceBasedSystem)
}
