package com.thesortinghat.staticcollector.domain.fetchers

import com.thesortinghat.staticcollector.domain.vo.FetchResponse

interface DataFetcher {
    fun run(url: String, filename: String): FetchResponse
}
