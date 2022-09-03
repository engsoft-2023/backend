package com.sortinghat.backend.data_collector.factories

import com.sortinghat.backend.data_collector.services.DockerComposeToModel
import com.sortinghat.backend.data_collector.services.FetchDockerCompose
import com.sortinghat.backend.data_collector.services.ParseDockerCompose
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
