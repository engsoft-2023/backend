package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.payloads.ServicesSyncAndAsyncOperationsPayload
import com.sortinghat.backend.domain.exceptions.EntityNotFoundException
import com.sortinghat.backend.domain.model.MessageChannel
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.domain.model.ServiceRepository
import org.springframework.beans.factory.annotation.Autowired

@org.springframework.stereotype.Service
class RegisterSyncAndAsyncOperationsImpl(
    @Autowired private val repository: ServiceRepository
) : RegisterSyncAndAsyncOperations {
    override fun execute(systemName: String, payload: ServicesSyncAndAsyncOperationsPayload) {
        val (operations) = payload
        val services = getAllServices(systemName)

        operations.forEach { (fromServiceName, syncAndAsync) ->
            val fromService = getService(services, fromServiceName)
            val (synchronous, asynchronous) = syncAndAsync

            processSynchronousOperations(services, fromService, synchronous)
            processAsynchronousOperations(services, fromService, asynchronous)
        }

        repository.saveAll(services)
    }

    private fun processSynchronousOperations(services: Set<Service>, fromService: Service, synchronousOperations: Map<String, Set<String>>) {
        synchronousOperations.forEach { (toServiceName, syncOperations) ->
            val toService = getService(services, toServiceName)

            syncOperations.forEach { syncOperation ->
                val operation = getExposedOperation(toService, syncOperation)
                fromService.consume(operation)
            }
        }
    }

    private fun processAsynchronousOperations(services: Set<Service>, fromService: Service, asynchronousOperations: Map<String, Set<String>>) {
        asynchronousOperations.forEach { (toServiceName, channelNames) ->
            val toService = getService(services, toServiceName)

            channelNames.forEach { channelName ->
                val channel = MessageChannel(channelName)

                toService.publishTo(channel)
                fromService.subscribeTo(channel)
            }
        }
    }

    private fun getAllServices(systemName: String): Set<Service> {
        val services = repository.findAllBySystem(systemName)

        if (services.isEmpty()) {
            throw EntityNotFoundException("unable to register operations: system $systemName not found")
        }

        return services
    }

    private fun getService(services: Set<Service>, targetName: String) =
        services.find { service -> service.name == targetName }
            ?: throw EntityNotFoundException("unable to register operations for service $targetName: service not found")

    private fun getExposedOperation(service: Service, operation: String) =
        service.exposedOperations.find { op -> op.toString() == operation }
            ?: throw EntityNotFoundException("unable to register operations for service ${service.name}: operation $operation not found for that service")
}
