package com.sortinghat.backend.data_collector.domain.factories

import com.sortinghat.backend.data_collector.domain.services.ConverterToModel
import com.sortinghat.backend.data_collector.domain.services.DataFetcher
import com.sortinghat.backend.data_collector.domain.services.DataParser

interface ExtractionComponentsAbstractFactory {
    fun createDataFetcher(): DataFetcher
    fun createDataParser(): DataParser
    fun createConverterToDomain(): ConverterToModel
}
