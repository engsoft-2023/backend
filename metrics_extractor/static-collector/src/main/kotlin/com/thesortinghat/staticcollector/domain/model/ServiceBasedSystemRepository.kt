package com.thesortinghat.staticcollector.domain.model

interface ServiceBasedSystemRepository {
    fun findById(id: String): ServiceBasedSystem?
    fun findByName(name: String): ServiceBasedSystem?
    fun save(system: ServiceBasedSystem)
}
