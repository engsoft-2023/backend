package com.sortinghat.static_collector.application.http.responses

import com.sortinghat.static_collector.domain.entities.platform_independent_model.Database
import com.sortinghat.static_collector.domain.entities.platform_independent_model.Service
import com.sortinghat.static_collector.domain.entities.platform_independent_model.System

class RegisterSystemResponse(system: System) {
    val id: String
    val name: String
    val services: List<Service>
    val databases: List<Database>
    val linksServicesDbs: List<ServiceDbLink>

    init {
        id = system.id
        name = system.name
        services = system.services()
        databases = system.databases()
        linksServicesDbs = system.dbServiceLinks().map {
                (db, service, payload) -> ServiceDbLink(db.id, service.id, payload)
        }
    }

    class ServiceDbLink(val dbId: String, val serviceId: String, val payload: Any?)
}
