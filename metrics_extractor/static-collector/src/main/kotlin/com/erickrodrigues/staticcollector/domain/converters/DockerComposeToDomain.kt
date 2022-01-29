package com.erickrodrigues.staticcollector.domain.converters

import com.erickrodrigues.staticcollector.domain.dockercompose.ContainerDecider
import com.erickrodrigues.staticcollector.domain.dockercompose.DockerCompose
import com.erickrodrigues.staticcollector.domain.dockercompose.DockerContainer
import com.erickrodrigues.staticcollector.domain.entities.Database
import com.erickrodrigues.staticcollector.domain.entities.Service
import com.erickrodrigues.staticcollector.domain.entities.ServiceBasedSystem
import com.erickrodrigues.staticcollector.domain.entities.SpecificTechnology
import com.erickrodrigues.staticcollector.domain.exceptions.UnableToConvertDataException

class DockerComposeToDomain : ConverterToDomain {
    private val containerToDatabase by lazy { hashMapOf<String, Database>() }

    override fun run(specificTechnology: SpecificTechnology): ServiceBasedSystem {
        try {
            val dockerCompose = specificTechnology as DockerCompose
            val system = ServiceBasedSystem(dockerCompose.name)
            val mapServices = dockerCompose.services!!.filter { ContainerDecider.isService(it.value) }
            val mapDatabases = dockerCompose.services!!.filter { ContainerDecider.isDatabase(it.value) }
            createDatabases(mapDatabases, system)
            createServices(mapServices, system)

            return system
        } catch (e: Exception) {
            throw UnableToConvertDataException("unable to convert docker-compose to a service-based system: ${e.message}")
        }
    }

    private fun createServices(mapServices: Map<String, DockerContainer>, system: ServiceBasedSystem) =
        mapServices.forEach { (name, serviceContainer) ->
            val service = Service(name)
            val dependsOn = serviceContainer.depends_on
            system.addService(service)
            dependsOn?.forEach { system.bindDatabaseToService(containerToDatabase[it]!!, service) }
        }

    private fun createDatabases(mapDatabases: Map<String, DockerContainer>, system: ServiceBasedSystem) =
        mapDatabases.forEach { (name, dbContainer) ->
            val dbModelAndMake = ContainerDecider.getDatabaseMakeAndModel(dbContainer.image!!) ?: return@forEach
            val (make, model) = dbModelAndMake
            val database = Database(name, make, model)
            system.addDatabase(database)
            containerToDatabase[name] = database
        }
}