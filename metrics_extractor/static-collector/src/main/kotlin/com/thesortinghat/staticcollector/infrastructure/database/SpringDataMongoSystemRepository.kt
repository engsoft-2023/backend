package com.thesortinghat.staticcollector.infrastructure.database

import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystem
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface SpringDataMongoSystemRepository : MongoRepository<ServiceBasedSystem, UUID> {
    fun findOneByName(name: String): ServiceBasedSystem?
}
