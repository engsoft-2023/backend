package com.sortinghat.backend.data_collector.application.config

import com.sortinghat.backend.data_collector.domain.factories.ExtractionComponentsAbstractFactory
import com.sortinghat.backend.data_collector.domain.services.ExtractData
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataCollectorConfiguration {

    @Bean
    fun extractDataService(factory: ExtractionComponentsAbstractFactory) = ExtractData(factory)
}
