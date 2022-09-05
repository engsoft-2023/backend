package com.sortinghat.backend.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication(scanBasePackages = ["com.sortinghat.backend"])
@EnableMongoRepositories(basePackages = ["com.sortinghat.backend"])
class BackendApplication

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}
