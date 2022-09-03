package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.SpecificTechnology
import com.sortinghat.backend.data_collector.exceptions.UnableToParseDataException
import com.sortinghat.backend.data_collector.utils.FetchResponse

interface DataParser {

    @Throws(UnableToParseDataException::class)
    fun execute(response: FetchResponse): SpecificTechnology
}
