package com.thesortinghat.staticcollector.domain.services

import com.thesortinghat.staticcollector.domain.exceptions.UnableToFetchDataException
import com.thesortinghat.staticcollector.domain.vo.FetchResponse

interface DataFetcher {

    @Throws(UnableToFetchDataException::class)
    fun execute(url: String, filename: String): FetchResponse
}
