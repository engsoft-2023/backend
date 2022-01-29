package com.erickrodrigues.staticcollector.domain.parsers

import com.erickrodrigues.staticcollector.domain.entities.SpecificTechnology
import com.erickrodrigues.staticcollector.domain.fetchers.FetchResponse

interface DataParser {
    fun run(response: FetchResponse): SpecificTechnology
}