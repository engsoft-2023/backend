package com.sortinghat.backend.data_collector.factories

import com.sortinghat.backend.data_collector.services.ConverterToDomain
import com.sortinghat.backend.data_collector.services.FetchDataFromRemoteRepository
import com.sortinghat.backend.data_collector.services.OpenApiToDomain
import com.sortinghat.backend.data_collector.services.ParseOpenApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("openApi")
class OpenApiCollectionComponentsFactory(
    @Autowired private val fetcher: FetchDataFromRemoteRepository,
    @Autowired private val parser: ParseOpenApi,
    @Autowired private val converter: OpenApiToDomain
) : CollectionComponentsFactory {

    override fun createDataFetcher() = fetcher

    override fun createDataParser() = parser

    @Suppress("UNCHECKED_CAST")
    override fun <Operation> createConverterToDomain(): ConverterToDomain<Operation> = converter as ConverterToDomain<Operation>
}
