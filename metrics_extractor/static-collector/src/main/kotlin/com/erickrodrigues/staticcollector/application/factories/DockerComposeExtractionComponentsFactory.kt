package com.erickrodrigues.staticcollector.application.factories

import com.erickrodrigues.staticcollector.application.http.HttpAdapter
import com.erickrodrigues.staticcollector.application.yaml.DockerComposeParser
import com.erickrodrigues.staticcollector.domain.converters.DockerComposeToDomain
import com.erickrodrigues.staticcollector.domain.factories.ExtractionComponentsAbstractFactory
import com.erickrodrigues.staticcollector.domain.fetchers.DockerComposeFetcher
import com.erickrodrigues.staticcollector.infrastructure.database.SpringDataMongoSystemRepository
import com.erickrodrigues.staticcollector.infrastructure.database.SystemMongoDbRepo
import com.erickrodrigues.staticcollector.infrastructure.kafka.SystemMessageQueue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class DockerComposeExtractionComponentsFactory : ExtractionComponentsAbstractFactory {

    @Autowired
    private val repo: SpringDataMongoSystemRepository? = null

    @Autowired
    private val kafkaTemplate: KafkaTemplate<String, Any>? = null

    @Value("\${kafka.topic.new.system}")
    private val newSystemTopicName: String? = null

    @Value("\${kafka.topic.new.service}")
    private val newServiceTopicName: String? = null

    @Value("\${kafka.topic.new.database}")
    private val newDatabaseTopicName: String? = null

    @Value("\${kafka.topic.new.usage}")
    private val newUsageTopicName: String? = null

    private val mapTopics by lazy {
        mapOf(
            "NewSystem" to newSystemTopicName,
            "NewService" to newServiceTopicName,
            "NewDatabase" to newDatabaseTopicName,
            "NewUsage" to newUsageTopicName
        )
    }

    override fun createDataFetcher() = DockerComposeFetcher(HttpAdapter())

    override fun createDataParser() = DockerComposeParser()

    override fun createConverterToDomain() = DockerComposeToDomain()

    override fun createServiceBasedSystemRepository() = SystemMongoDbRepo(repo!!)

    override fun createMessageBroker() = SystemMessageQueue(kafkaTemplate!!, mapTopics)
}
