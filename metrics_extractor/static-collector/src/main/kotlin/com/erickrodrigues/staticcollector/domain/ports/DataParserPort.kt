package com.erickrodrigues.staticcollector.domain.ports

import com.erickrodrigues.staticcollector.domain.entities.SpecificTechnology
import com.erickrodrigues.staticcollector.domain.vo.FetchResponse

interface DataParserPort {
    fun run(response: FetchResponse): SpecificTechnology
}