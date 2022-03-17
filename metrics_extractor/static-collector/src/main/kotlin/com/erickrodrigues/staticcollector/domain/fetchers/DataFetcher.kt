package com.erickrodrigues.staticcollector.domain.fetchers

import com.erickrodrigues.staticcollector.domain.vo.FetchResponse

interface DataFetcher {
    fun run(url: String): FetchResponse
}