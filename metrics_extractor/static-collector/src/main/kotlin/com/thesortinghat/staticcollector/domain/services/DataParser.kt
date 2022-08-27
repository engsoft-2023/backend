package com.thesortinghat.staticcollector.domain.services

import com.thesortinghat.staticcollector.domain.exceptions.UnableToParseDataException
import com.thesortinghat.staticcollector.domain.model.SpecificTechnology
import com.thesortinghat.staticcollector.domain.vo.FetchResponse

interface DataParser {

    @Throws(UnableToParseDataException::class)
    fun execute(response: FetchResponse): SpecificTechnology
}
