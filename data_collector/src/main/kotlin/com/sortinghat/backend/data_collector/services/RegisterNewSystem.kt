package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.domain.model.Service

interface RegisterNewSystem {
    fun execute(url: String, filename: String): Set<Service>
}
