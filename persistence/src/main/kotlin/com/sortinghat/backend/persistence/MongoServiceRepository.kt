package com.sortinghat.backend.persistence

import org.springframework.data.mongodb.repository.MongoRepository

interface MongoServiceRepository : MongoRepository<ServiceSchema, String> {
    fun findAllBySystemName(name: String): Set<ServiceSchema>
}
