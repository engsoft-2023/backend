package com.thesortinghat.staticcollector.application.factories

import com.thesortinghat.staticcollector.application.adapters.HttpAdapter
import com.thesortinghat.staticcollector.application.adapters.DockerComposeParserAdapter
import com.thesortinghat.staticcollector.domain.converters.DockerComposeToDomain
import com.thesortinghat.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.thesortinghat.staticcollector.domain.fetchers.DockerComposeFetcher
import com.thesortinghat.staticcollector.domain.ports.MessageBroker
import com.thesortinghat.staticcollector.domain.ports.ServiceBasedSystemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DockerComposeExtractionComponentsFactory : ExtractionComponentsAbstractFactory {

    @Autowired
    private lateinit var mongoDbRepo: ServiceBasedSystemRepository

    @Autowired
    private lateinit var messageQueue: MessageBroker

    override fun createDataFetcher() = DockerComposeFetcher(HttpAdapter())

    override fun createDataParser() = DockerComposeParserAdapter()

    override fun createConverterToDomain() = DockerComposeToDomain()

    override fun createServiceBasedSystemRepository() = mongoDbRepo

    override fun createMessageBroker() = messageQueue
}
