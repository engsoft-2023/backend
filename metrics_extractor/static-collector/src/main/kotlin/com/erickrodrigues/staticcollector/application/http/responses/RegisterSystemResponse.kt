package com.erickrodrigues.staticcollector.application.http.responses

import com.erickrodrigues.staticcollector.domain.entities.Database
import com.erickrodrigues.staticcollector.domain.entities.Service
import com.erickrodrigues.staticcollector.domain.entities.ServiceBasedSystem

class RegisterSystemResponse(system: ServiceBasedSystem) {
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
        linksServicesDbs = system.linksDatabasesServices().map {
                ServiceDbLink(it.db.id, it.service.id, it.payload)
        }
    }

    class ServiceDbLink(val dbId: String, val serviceId: String, val payload: Any?)
}