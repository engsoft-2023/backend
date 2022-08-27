package com.thesortinghat.staticcollector.application.factories

import com.thesortinghat.staticcollector.application.services.ParseDockerCompose
import com.thesortinghat.staticcollector.domain.dockercompose.DockerComposeToModel
import com.thesortinghat.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.thesortinghat.staticcollector.application.services.FetchDockerCompose
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
