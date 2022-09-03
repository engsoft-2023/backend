package com.sortinghat.backend.data_collector.factories

import com.sortinghat.backend.data_collector.services.ConverterToModel
import com.sortinghat.backend.data_collector.services.DataFetcher
import com.sortinghat.backend.data_collector.services.DataParser

interface ExtractionComponentsAbstractFactory {
    fun createDataFetcher(): DataFetcher
    fun createDataParser(): DataParser
    fun createConverterToDomain(): ConverterToModel
}
