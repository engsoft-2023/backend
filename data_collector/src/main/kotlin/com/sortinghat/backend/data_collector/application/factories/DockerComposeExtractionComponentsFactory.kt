package com.sortinghat.backend.data_collector.application.factories

import com.sortinghat.backend.data_collector.application.services.ParseDockerCompose
import com.sortinghat.backend.data_collector.domain.dockercompose.DockerComposeToModel
import com.sortinghat.backend.data_collector.domain.factories.ExtractionComponentsAbstractFactory
import com.sortinghat.backend.data_collector.application.services.FetchDockerCompose
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DockerComposeExtractionComponentsFactory(
        @Autowired private val fetcher: FetchDockerCompose,
        @Autowired private val parser: ParseDockerCompose,
): ExtractionComponentsAbstractFactory {

    override fun createDataFetcher() = fetcher

    override fun createDataParser() = parser

    override fun createConverterToDomain() = DockerComposeToModel()
}
