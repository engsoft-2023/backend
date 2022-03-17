package com.erickrodrigues.staticcollector.infrastructure.database

import org.springframework.data.mongodb.repository.MongoRepository

interface SpringDataMongoSystemRepository : MongoRepository<SystemSchema, String> {
    fun findOneByNameIn(name: String): SystemSchema?
}
