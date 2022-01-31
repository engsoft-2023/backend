package com.erickrodrigues.staticcollector.infrastructure.kafka

import com.erickrodrigues.staticcollector.domain.events.NewDatabase
import com.erickrodrigues.staticcollector.domain.events.NewService
import com.erickrodrigues.staticcollector.domain.events.NewSystem
import com.erickrodrigues.staticcollector.domain.events.NewUsage
import com.erickrodrigues.staticcollector.domain.ports.MessageBroker
import org.springframework.kafka.core.KafkaTemplate

class SystemMessageQueue(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val kafkaTopics: Map<String, String?>
) : MessageBroker {

    override fun newSystem(s: NewSystem) {
        kafkaTemplate.send(kafkaTopics["NewSystem"]!!, s)
    }

    override fun newService(s: NewService) {
        kafkaTemplate.send(kafkaTopics["NewService"]!!, s)
    }

    override fun newDatabase(db: NewDatabase) {
        kafkaTemplate.send(kafkaTopics["NewDatabase"]!!, db)
    }

    override fun newUsage(link: NewUsage) {
        kafkaTemplate.send(kafkaTopics["NewUsage"]!!, link)
    }
}