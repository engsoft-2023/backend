package com.sortinghat.static_collector.domain.ports.repositories

import com.sortinghat.static_collector.domain.entities.platform_independent_model.System

interface SystemRepository {
    fun save(system: System)

    fun findByName(name: String): System?
}