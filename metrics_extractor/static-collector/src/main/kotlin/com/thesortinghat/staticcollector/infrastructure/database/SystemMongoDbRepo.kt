package com.thesortinghat.staticcollector.infrastructure.database

import com.thesortinghat.staticcollector.domain.model.Database
import com.thesortinghat.staticcollector.domain.model.Service
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystemRepository
import org.springframework.stereotype.Component

@Component
class SystemMongoDbRepo(private val repo: SpringDataMongoSystemRepository) : ServiceBasedSystemRepository {

    override fun findById(id: String): ServiceBasedSystem? {
        val s = repo.findById(id)
        return convertMongoObjectToDomain(s.orElse(null))
    }

    override fun findByName(name: String): ServiceBasedSystem? {
        val s = repo.findOneByName(name)
        return convertMongoObjectToDomain(s)
    }

    override fun save(system: ServiceBasedSystem) {
        repo.save(SystemSchema.createFromDomain(system))
    }

    private fun convertMongoObjectToDomain(s: SystemSchema?): ServiceBasedSystem? {
        if (s == null) return null

        val domainSystem = ServiceBasedSystem(s.id, s.name)
        val mapServices = s.services.associate { it.id to Service(it.id, it.name) }
        val mapDbs = s.databases.associate { it.id to Database(it.id, it.name, it.make, it.model) }
        s.databasesUsages.forEach {
            domainSystem.addDatabaseUsage(mapDbs[it.dbId]!!, mapServices[it.serviceId]!!, it.payload)
        }

        return domainSystem
    }
}
