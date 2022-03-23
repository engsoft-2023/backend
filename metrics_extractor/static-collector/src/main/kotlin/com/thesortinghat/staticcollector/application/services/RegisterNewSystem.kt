package com.thesortinghat.staticcollector.application.services

import com.thesortinghat.staticcollector.infrastructure.kafka.MessageQueue
import com.thesortinghat.staticcollector.infrastructure.kafka.NewDatabase
import com.thesortinghat.staticcollector.infrastructure.kafka.NewService
import com.thesortinghat.staticcollector.infrastructure.kafka.NewSystem
import com.thesortinghat.staticcollector.infrastructure.kafka.NewUsage
import com.thesortinghat.staticcollector.application.exceptions.EntityAlreadyExistsException
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystemRepository
import com.thesortinghat.staticcollector.domain.services.ExtractData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RegisterNewSystem(
        @Autowired private val extractData: ExtractData,
        @Autowired private val repository: ServiceBasedSystemRepository,
        @Autowired private val messageQueue: MessageQueue
) {
    fun execute(url: String, filename: String): ServiceBasedSystem {
        val system = extractData.execute(url, filename)
        persistData(system)
        sendDataToMessageQueue(system)
        return system
    }

    private fun persistData(system: ServiceBasedSystem) {
        val sys = repository.findByName(system.name)

        if (sys != null)
            throw EntityAlreadyExistsException("system with that name already exists")

        repository.save(system)
    }

    private fun sendDataToMessageQueue(system: ServiceBasedSystem) {
        messageQueue.newSystem(NewSystem(system.id, system.name))
        system.services().forEach { messageQueue.newService(NewService(it.id, it.name, system.id)) }
        system.databases().forEach { messageQueue.newDatabase(NewDatabase(it.id, it.make)) }
        system.databasesUsages().forEach { messageQueue.newUsage(NewUsage(it.service.id, it.db.id)) }
    }
}
