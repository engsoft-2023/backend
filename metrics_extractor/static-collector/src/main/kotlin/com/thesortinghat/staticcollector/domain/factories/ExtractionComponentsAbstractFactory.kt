package com.thesortinghat.staticcollector.domain.factories

import com.thesortinghat.staticcollector.domain.converters.ConverterToDomain
import com.thesortinghat.staticcollector.domain.fetchers.DataFetcher
import com.thesortinghat.staticcollector.domain.ports.DataParserPort
import com.thesortinghat.staticcollector.domain.ports.MessageBroker
import com.thesortinghat.staticcollector.domain.ports.ServiceBasedSystemRepository

interface ExtractionComponentsAbstractFactory {
    fun createDataFetcher(): DataFetcher
    fun createDataParser(): DataParserPort
    fun createConverterToDomain(): ConverterToDomain
    fun createServiceBasedSystemRepository(): ServiceBasedSystemRepository
    fun createMessageBroker(): MessageBroker
}
