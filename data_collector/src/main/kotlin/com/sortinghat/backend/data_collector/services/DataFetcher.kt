package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.exceptions.UnableToFetchDataException
import com.sortinghat.backend.data_collector.utils.FetchResponse

interface DataFetcher {

    @Throws(UnableToFetchDataException::class)
    fun execute(url: String, filename: String): FetchResponse
}
