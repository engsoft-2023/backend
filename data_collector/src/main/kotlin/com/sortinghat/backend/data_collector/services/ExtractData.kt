package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.factories.ExtractionComponentsAbstractFactory
import com.sortinghat.backend.domain.model.Service

@org.springframework.stereotype.Service
class ExtractData(factory: ExtractionComponentsAbstractFactory) {
    private val fetcher: DataFetcher
    private val parser: DataParser
    private val converter: ConverterToModel

    init {
        fetcher = factory.createDataFetcher()
        parser = factory.createDataParser()
        converter = factory.createConverterToDomain()
    }

    fun execute(url: String, filename: String): Set<Service> {
        val response = fetcher.execute(url, filename)
        val specificTechnology = parser.execute(response)
        return converter.execute(specificTechnology)
    }
}
