package domain.ports.repositories

import domain.entities.platform_independent_model.System

interface SystemRepository {
    fun save(system: System)

    fun findByName(name: String): System?
}