package com.thesortinghat.staticcollector.infrastructure.database

import com.thesortinghat.staticcollector.domain.entities.Database
import com.thesortinghat.staticcollector.domain.entities.DatabaseUsage
import com.thesortinghat.staticcollector.domain.entities.Service
import com.thesortinghat.staticcollector.domain.entities.ServiceBasedSystem
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

data class ServiceSchema(
    @Id
    val id: String,
    val name: String
) {
    companion object {
        fun createFromDomain(s: Service) =
            ServiceSchema(
                id = s.id,
                name = s.name
            )
    }
}

data class DbSchema(
    @Id
    val id: String,
    val name: String,
    val make: String,
    val model: String
) {
    companion object {
        fun createFromDomain(db: Database) =
            DbSchema(
                id = db.id,
                name = db.name,
                make = db.make,
                model = db.model
            )
    }
}

data class DatabaseUsageSchema(
    val dbId: String,
    val serviceId: String,
    val payload: Any?
) {
    companion object {
        fun createFromDomain(link: DatabaseUsage) =
            DatabaseUsageSchema(
                dbId = link.db.id,
                serviceId = link.service.id,
                payload = link.payload
            )
    }
}

@Document
data class SystemSchema(
    @Id
    val id: String,
    val name: String,
    val services: List<ServiceSchema>,
    val databases: List<DbSchema>,
    val databasesUsages: List<DatabaseUsageSchema>
) {
    companion object {
        fun createFromDomain(s: ServiceBasedSystem) =
            SystemSchema(
                id = s.id,
                name = s.name,
                services = s.services().map { ServiceSchema.createFromDomain(it) },
                databases = s.databases().map { DbSchema.createFromDomain(it) },
                databasesUsages = s.databasesUsages().map { DatabaseUsageSchema.createFromDomain(it) }
            )
    }
}
