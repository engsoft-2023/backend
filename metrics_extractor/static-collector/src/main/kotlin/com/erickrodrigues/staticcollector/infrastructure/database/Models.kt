package com.erickrodrigues.staticcollector.infrastructure.database

import com.erickrodrigues.staticcollector.domain.entities.Database
import com.erickrodrigues.staticcollector.domain.entities.DbServiceEdge
import com.erickrodrigues.staticcollector.domain.entities.Service
import com.erickrodrigues.staticcollector.domain.entities.ServiceBasedSystem
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

data class AppService(
    @Id
    val id: String,
    val name: String
) {
    companion object {
        fun createFromDomain(s: Service) =
            AppService(
                id = s.id,
                name = s.name
            )
    }
}

data class AppDb(
    @Id
    val id: String,
    val name: String,
    val make: String,
    val model: String
) {
    companion object {
        fun createFromDomain(db: Database) =
            AppDb(
                id = db.id,
                name = db.name,
                make = db.make,
                model = db.model
            )
    }
}

data class AppLinkDbService(
    val dbId: String,
    val serviceId: String,
    val payload: Any?
) {
    companion object {
        fun createFromDomain(link: DbServiceEdge) =
            AppLinkDbService(
                dbId = link.db.id,
                serviceId = link.service.id,
                payload = link.payload
            )
    }
}

@Document
data class System(
    @Id
    val id: String,
    val name: String,
    val services: List<AppService>,
    val databases: List<AppDb>,
    val linksDbService: List<AppLinkDbService>
) {
    companion object {
        fun createFromDomain(s: ServiceBasedSystem) =
            System(
                id = s.id,
                name = s.name,
                services = s.services().map { AppService.createFromDomain(it) },
                databases = s.databases().map { AppDb.createFromDomain(it) },
                linksDbService = s.linksDatabasesServices().map { AppLinkDbService.createFromDomain(it) }
            )
    }
}
