package com.thesortinghat.staticcollector.infrastructure.kafka

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaMessageQueue(@Autowired private val kafkaTemplate: KafkaTemplate<String, Any>) : MessageQueue {

    @Value("\${kafka.topic.new.system}")
    private lateinit var newSystemTopicName: String

    @Value("\${kafka.topic.new.service}")
    private lateinit var newServiceTopicName: String

    @Value("\${kafka.topic.new.database}")
    private lateinit var newDatabaseTopicName: String

    @Value("\${kafka.topic.new.usage}")
    private lateinit var newUsageTopicName: String

    override fun newSystem(s: NewSystem) {
        kafkaTemplate.send(newSystemTopicName, s)
    }

    override fun newService(s: NewService) {
        kafkaTemplate.send(newServiceTopicName, s)
    }

    override fun newDatabase(db: NewDatabase) {
        kafkaTemplate.send(newDatabaseTopicName, db)
    }

    override fun newUsage(link: NewUsage) {
        kafkaTemplate.send(newUsageTopicName, link)
    }
}
