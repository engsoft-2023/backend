package com.thesortinghat.staticcollector.domain.factories

import com.thesortinghat.staticcollector.domain.services.ConverterToModel
import com.thesortinghat.staticcollector.domain.services.DataFetcher
import com.thesortinghat.staticcollector.domain.services.DataParser

interface ExtractionComponentsAbstractFactory {
    fun createDataFetcher(): DataFetcher
    fun createDataParser(): DataParser
    fun createConverterToDomain(): ConverterToModel
}
