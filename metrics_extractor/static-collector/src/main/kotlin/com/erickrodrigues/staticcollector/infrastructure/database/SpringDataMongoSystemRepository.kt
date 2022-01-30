package com.erickrodrigues.staticcollector.infrastructure.database

import org.springframework.data.mongodb.repository.MongoRepository

interface SpringDataMongoSystemRepository : MongoRepository<System, String> {
    fun findOneByNameIn(name: String): System?
}
