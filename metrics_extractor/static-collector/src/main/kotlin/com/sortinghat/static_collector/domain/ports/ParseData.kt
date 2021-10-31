package com.sortinghat.static_collector.domain.ports

import com.sortinghat.static_collector.domain.entities.platform_specific_model.PlatformSpecificModel

interface ParseData {
    fun run(data: String): PlatformSpecificModel
}