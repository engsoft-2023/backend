package com.erickrodrigues.staticcollector.domain.fetchers

interface DataFetcher {
    fun run(url: String): FetchResponse
}