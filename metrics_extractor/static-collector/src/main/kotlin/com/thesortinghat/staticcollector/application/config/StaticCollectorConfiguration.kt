package com.thesortinghat.staticcollector.application.config

import com.thesortinghat.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.thesortinghat.staticcollector.domain.ports.ServiceBasedSystemRepository
import com.thesortinghat.staticcollector.domain.services.ExtractDataService
import com.thesortinghat.staticcollector.domain.services.GetSystemService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StaticCollectorConfiguration {

    @Bean
    fun extractDataService(extractFactory: ExtractionComponentsAbstractFactory) = ExtractDataService(extractFactory)

    @Bean
    fun getSystemService(repo: ServiceBasedSystemRepository) = GetSystemService(repo)
}
