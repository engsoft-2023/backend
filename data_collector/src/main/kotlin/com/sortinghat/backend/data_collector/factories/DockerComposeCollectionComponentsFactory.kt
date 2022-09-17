package com.sortinghat.backend.data_collector.factories

import com.sortinghat.backend.data_collector.services.ConverterToDomain
import com.sortinghat.backend.data_collector.services.DockerComposeToDomain
import com.sortinghat.backend.data_collector.services.FetchDataFromRemoteRepository
import com.sortinghat.backend.data_collector.services.ParseDockerCompose
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DockerComposeCollectionComponentsFactory(
    @Autowired private val fetcher: FetchDataFromRemoteRepository,
    @Autowired private val parser: ParseDockerCompose,
    @Autowired private val converter: DockerComposeToDomain,
): CollectionComponentsFactory {

    override fun createDataFetcher() = fetcher

    override fun createDataParser() = parser

    @Suppress("UNCHECKED_CAST")
    override fun <Service> createConverterToDomain() = converter as ConverterToDomain<Service>
}
