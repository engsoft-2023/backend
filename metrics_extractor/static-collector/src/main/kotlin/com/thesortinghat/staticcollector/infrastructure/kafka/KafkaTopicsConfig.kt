package com.thesortinghat.staticcollector.infrastructure.kafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaTopicsConfig {

    @Value("\${kafka.topic.new.system}")
    private val newSystemTopicName: String? = null

    @Value("\${kafka.topic.new.service}")
    private val newServiceTopicName: String? = null

    @Value("\${kafka.topic.new.database}")
    private val newDatabaseTopicName: String? = null

    @Value("\${kafka.topic.new.usage}")
    private val newUsageTopicName: String? = null

    @Bean
    fun newSystemTopic() = NewTopic(newSystemTopicName, 1, 1.toShort())

    @Bean
    fun newServiceTopic() = NewTopic(newServiceTopicName, 1, 1.toShort())

    @Bean
    fun newDatabaseTopic() = NewTopic(newDatabaseTopicName, 1, 1.toShort())

    @Bean
    fun newUsageTopic() = NewTopic(newUsageTopicName, 1, 1.toShort())
}