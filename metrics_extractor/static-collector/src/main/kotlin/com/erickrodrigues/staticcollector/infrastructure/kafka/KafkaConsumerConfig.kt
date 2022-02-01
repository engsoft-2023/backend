package com.erickrodrigues.staticcollector.infrastructure.kafka

import com.erickrodrigues.staticcollector.domain.events.NewDatabase
import com.erickrodrigues.staticcollector.domain.events.NewService
import com.erickrodrigues.staticcollector.domain.events.NewSystem
import com.erickrodrigues.staticcollector.domain.events.NewUsage
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Value("\${spring.kafka.consumer.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    /* ----------------------- NEW SYSTEM CONSUMER ----------------------- */
    @Bean
    fun newSystemConsumerFactory(): ConsumerFactory<String, NewSystem> {
        val props = hashMapOf<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = "group-id"
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*"
        return DefaultKafkaConsumerFactory(props, StringDeserializer(), JsonDeserializer(NewSystem::class.java))
    }

    @Bean
    fun newSystemKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, NewSystem> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, NewSystem>()
        factory.consumerFactory = newSystemConsumerFactory()
        return factory
    }

    /* ----------------------- NEW SERVICE CONSUMER ----------------------- */
    @Bean
    fun newServiceConsumerFactory(): ConsumerFactory<String, NewService> {
        val props = hashMapOf<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = "group-id"
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*"
        return DefaultKafkaConsumerFactory(props, StringDeserializer(), JsonDeserializer(NewService::class.java))
    }

    @Bean
    fun newServiceKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, NewService> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, NewService>()
        factory.consumerFactory = newServiceConsumerFactory()
        return factory
    }

    /* ----------------------- NEW DATABASE CONSUMER ----------------------- */
    @Bean
    fun newDatabaseConsumerFactory(): ConsumerFactory<String, NewDatabase> {
        val props = hashMapOf<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = "group-id"
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*"
        return DefaultKafkaConsumerFactory(props, StringDeserializer(), JsonDeserializer(NewDatabase::class.java))
    }

    @Bean
    fun newDatabaseKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, NewDatabase> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, NewDatabase>()
        factory.consumerFactory = newDatabaseConsumerFactory()
        return factory
    }

    /* ----------------------- NEW USAGE CONSUMER ----------------------- */
    @Bean
    fun newUsageConsumerFactory(): ConsumerFactory<String, NewUsage> {
        val props = hashMapOf<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = "group-id"
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*"
        return DefaultKafkaConsumerFactory(props, StringDeserializer(), JsonDeserializer(NewUsage::class.java))
    }

    @Bean
    fun newUsageKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, NewUsage> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, NewUsage>()
        factory.consumerFactory = newUsageConsumerFactory()
        return factory
    }
}
