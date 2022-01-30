package com.erickrodrigues.staticcollector.infrastructure.database

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

data class AppService(
    @Id
    val id: String,
    val name: String
)

data class AppDb(
    @Id
    val id: String,
    val name: String,
    val make: String,
    val model: String
)

data class AppLinkDbService(
    val dbId: String,
    val serviceId: String,
    val payload: Any?
)

@Document
data class System(
    @Id
    val id: String,
    val name: String,
    val services: List<AppService>,
    val databases: List<AppDb>,
    val linksDbService: List<AppLinkDbService>
)
