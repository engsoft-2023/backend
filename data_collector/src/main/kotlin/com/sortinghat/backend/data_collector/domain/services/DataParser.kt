package com.sortinghat.backend.data_collector.domain.services

import com.sortinghat.backend.data_collector.domain.exceptions.UnableToParseDataException
import com.sortinghat.backend.data_collector.domain.model.SpecificTechnology
import com.sortinghat.backend.data_collector.domain.vo.FetchResponse

interface DataParser {

    @Throws(UnableToParseDataException::class)
    fun execute(response: FetchResponse): SpecificTechnology
}
