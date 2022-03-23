package com.thesortinghat.staticcollector.infrastructure.database

import org.springframework.data.mongodb.repository.MongoRepository

interface SpringDataMongoSystemRepository : MongoRepository<SystemSchema, String> {
    fun findOneByName(name: String): SystemSchema?
}
