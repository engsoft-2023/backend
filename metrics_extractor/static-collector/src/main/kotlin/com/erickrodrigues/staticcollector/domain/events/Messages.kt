package com.erickrodrigues.staticcollector.domain.events

data class NewSystem(
    val id: String,
    val name: String
)

data class NewService(
    val id: String,
    val name: String,
    val systemID: String
)

data class NewDatabase(
    val id: String,
    val make: String
)

data class NewUsage(
    val serviceID: String,
    val databaseID: String
)
