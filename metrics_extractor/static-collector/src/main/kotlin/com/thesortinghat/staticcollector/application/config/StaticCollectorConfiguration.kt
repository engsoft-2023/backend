package com.thesortinghat.staticcollector.application.config

import com.thesortinghat.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.thesortinghat.staticcollector.domain.services.ExtractData
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StaticCollectorConfiguration {

    @Bean
    fun extractDataService(factory: ExtractionComponentsAbstractFactory) = ExtractData(factory)
}
