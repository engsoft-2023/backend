package com.thesortinghat.staticcollector.infrastructure.database

import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystemRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class SystemMongoDbRepo(private val repo: SpringDataMongoSystemRepository) : ServiceBasedSystemRepository {

    override fun findById(id: String): ServiceBasedSystem? {
        return repo.findById(UUID.fromString(id)).orElse(null)
    }

    override fun findByName(name: String): ServiceBasedSystem? {
        return repo.findOneByName(name)
    }

    override fun save(system: ServiceBasedSystem) {
        repo.save(system)
    }
}
