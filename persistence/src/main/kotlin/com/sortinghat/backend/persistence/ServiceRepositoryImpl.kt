package com.sortinghat.backend.persistence

import com.sortinghat.backend.domain.model.Database
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.domain.model.ServiceBasedSystem
import com.sortinghat.backend.domain.model.ServiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ServiceRepositoryImpl(@Autowired private val repo: MongoServiceRepository) : ServiceRepository {

    override fun findAllBySystem(id: String): Set<Service> {
        return servicesSchemaToDomain(repo.findAllBySystemName(id))
    }

    override fun findAllSystems(): Set<ServiceBasedSystem> {
        val services = repo.findAll()
        return services.fold(setOf()) { systems, service -> systems + service.system }
    }

    override fun saveAll(services: Set<Service>) {
        repo.saveAll(services.map { s -> ServiceSchema.toSchema(s) })
    }

    private fun servicesSchemaToDomain(schemas: Set<ServiceSchema>): Set<Service> {
        val dbsSchema = schemas.fold(setOf<DbSchema>()) { dbs, schema -> dbs + schema.databasesUsages }
        val services = schemas
            .map { schema ->
                Service(
                    name = schema.name,
                    responsibility = schema.responsibility,
                    module = schema.module,
                    system = schema.system,
                    exposedOperations = schema.exposedOperations.toMutableSet(),
                    consumedOperations = schema.consumedOperations.toMutableSet(),
                    channelsPublishing = schema.channelsPublishing.toMutableSet(),
                    channelsSubscribing = schema.channelsSubscribing.toMutableSet()
                )
            }
            .toSet()

        dbsSchema.forEach { schema ->
            val db = Database(
                namespace = schema.namespace,
                model = schema.model
            )

            schema.usedBy.forEach { (serviceName, accessType) ->
                val service = services.find { s -> s.name == serviceName }!!
                service.addUsage(db, accessType)
                db.addUsage(service, accessType)
            }
        }

        return services
    }
}
