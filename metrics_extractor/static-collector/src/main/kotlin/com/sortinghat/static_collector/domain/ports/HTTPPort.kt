package com.sortinghat.static_collector.domain.ports

import com.sortinghat.static_collector.domain.responses.HTTPResponse

interface HTTPPort {
    fun get(url: String): HTTPResponse
}