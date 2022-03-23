package com.thesortinghat.staticcollector.domain.services

import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.factories.ExtractionComponentsAbstractFactory

class ExtractData(factory: ExtractionComponentsAbstractFactory) {
    private val fetcher: DataFetcher
    private val parser: DataParser
    private val converter: ConverterToModel

    init {
        fetcher = factory.createDataFetcher()
        parser = factory.createDataParser()
        converter = factory.createConverterToDomain()
    }

    fun execute(url: String, filename: String): ServiceBasedSystem {
        val response = fetcher.execute(url, filename)
        val specificTechnology = parser.execute(response)
        return converter.execute(specificTechnology)
    }
}
