package com.sortinghat.static_collector.domain.ports

import com.sortinghat.static_collector.domain.entities.platform_specific_model.PlatformSpecificModel
import com.sortinghat.static_collector.domain.fetchers.FetchResponse

interface ParseData {
    fun run(fetchResponse: FetchResponse): PlatformSpecificModel
}