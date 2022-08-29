package com.sortinghat.backend.data_collector.domain.services

import com.sortinghat.backend.data_collector.domain.exceptions.UnableToFetchDataException
import com.sortinghat.backend.data_collector.domain.vo.FetchResponse

interface DataFetcher {

    @Throws(UnableToFetchDataException::class)
    fun execute(url: String, filename: String): FetchResponse
}
