package com.erickrodrigues.staticcollector.domain.ports

import com.erickrodrigues.staticcollector.domain.entities.ServiceBasedSystem

interface ServiceBasedSystemRepository {
    fun findByName(name: String): ServiceBasedSystem?
    fun save(system: ServiceBasedSystem)
}