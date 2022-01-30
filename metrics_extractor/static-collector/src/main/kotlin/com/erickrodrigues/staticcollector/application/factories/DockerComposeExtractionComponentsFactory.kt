package com.erickrodrigues.staticcollector.application.factories

import com.erickrodrigues.staticcollector.application.http.HttpAdapter
import com.erickrodrigues.staticcollector.application.yaml.DockerComposeParser
import com.erickrodrigues.staticcollector.domain.converters.DockerComposeToDomain
import com.erickrodrigues.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.erickrodrigues.staticcollector.domain.fetchers.DockerComposeFetcher
import com.erickrodrigues.staticcollector.infrastructure.database.SpringDataMongoSystemRepository
import com.erickrodrigues.staticcollector.infrastructure.database.SystemMongoDbRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DockerComposeExtractionComponentsFactory : ExtractionComponentsAbstractFactory {

    @Autowired
    private lateinit var repo: SpringDataMongoSystemRepository

    override fun createDataFetcher() = DockerComposeFetcher(HttpAdapter())

    override fun createDataParser() = DockerComposeParser()

    override fun createConverterToDomain() = DockerComposeToDomain()

    override fun createServiceBasedSystemRepository() = SystemMongoDbRepo(repo)
}
