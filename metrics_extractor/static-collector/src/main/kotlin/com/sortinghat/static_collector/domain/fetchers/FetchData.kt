package com.sortinghat.static_collector.domain.fetchers

interface FetchData {
    fun run(url: String): String
}