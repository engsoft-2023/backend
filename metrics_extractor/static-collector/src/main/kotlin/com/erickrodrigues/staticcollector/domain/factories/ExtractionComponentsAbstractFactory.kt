package com.erickrodrigues.staticcollector.domain.factories

import com.erickrodrigues.staticcollector.domain.converters.ConverterToDomain
import com.erickrodrigues.staticcollector.domain.fetchers.DataFetcher
import com.erickrodrigues.staticcollector.domain.parsers.DataParser
import com.erickrodrigues.staticcollector.domain.ports.MessageBroker
import com.erickrodrigues.staticcollector.domain.ports.ServiceBasedSystemRepository

interface ExtractionComponentsAbstractFactory {
    fun createDataFetcher(): DataFetcher
    fun createDataParser(): DataParser
    fun createConverterToDomain(): ConverterToDomain
    fun createServiceBasedSystemRepository(): ServiceBasedSystemRepository
    fun createMessageBroker(): MessageBroker
}
