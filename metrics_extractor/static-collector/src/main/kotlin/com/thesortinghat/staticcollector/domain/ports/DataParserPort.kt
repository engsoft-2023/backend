package com.thesortinghat.staticcollector.domain.ports

import com.thesortinghat.staticcollector.domain.entities.SpecificTechnology
import com.thesortinghat.staticcollector.domain.vo.FetchResponse

interface DataParserPort {
    fun run(response: FetchResponse): SpecificTechnology
}