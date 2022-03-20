package com.thesortinghat.staticcollector.infrastructure.kafka

import com.thesortinghat.staticcollector.domain.events.NewDatabase
import com.thesortinghat.staticcollector.domain.events.NewService
import com.thesortinghat.staticcollector.domain.events.NewSystem
import com.thesortinghat.staticcollector.domain.events.NewUsage
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SimpleKafkaConsumerListener {

    @KafkaListener(
        topics = ["\${kafka.topic.new.system}"],
        groupId = "groupId",
        containerFactory = "newSystemKafkaListenerContainerFactory"
    )
    fun onNewSystemEvent(message: NewSystem) {
        println("New system received: $message")
    }

    @KafkaListener(
        topics = ["\${kafka.topic.new.service}"],
        groupId = "groupId",
        containerFactory = "newServiceKafkaListenerContainerFactory"
    )
    fun onNewServiceEvent(message: NewService) {
        println("New service received: $message")
    }

    @KafkaListener(
        topics = ["\${kafka.topic.new.database}"],
        groupId = "groupId",
        containerFactory = "newDatabaseKafkaListenerContainerFactory"
    )
    fun onNewDatabaseEvent(message: NewDatabase) {
        println("New database received: $message")
    }

    @KafkaListener(
        topics = ["\${kafka.topic.new.usage}"],
        groupId = "groupId",
        containerFactory = "newUsageKafkaListenerContainerFactory"
    )
    fun onNewUsageEvent(message: NewUsage) {
        println("New usage received: $message")
    }
}
