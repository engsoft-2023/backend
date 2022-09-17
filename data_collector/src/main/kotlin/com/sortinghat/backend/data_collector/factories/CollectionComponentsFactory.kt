package com.sortinghat.backend.data_collector.factories

import com.sortinghat.backend.data_collector.services.ConverterToDomain
import com.sortinghat.backend.data_collector.services.DataFetcher
import com.sortinghat.backend.data_collector.services.DataParser

interface CollectionComponentsFactory {
    fun createDataFetcher(): DataFetcher
    fun createDataParser(): DataParser
    fun <T> createConverterToDomain(): ConverterToDomain<T>
}
