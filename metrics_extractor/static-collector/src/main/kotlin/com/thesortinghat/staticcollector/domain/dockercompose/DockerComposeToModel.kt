package com.thesortinghat.staticcollector.domain.dockercompose

import com.thesortinghat.staticcollector.domain.model.Database
import com.thesortinghat.staticcollector.domain.model.Service
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.model.SpecificTechnology
import com.thesortinghat.staticcollector.domain.exceptions.UnableToConvertDataException
import com.thesortinghat.staticcollector.domain.services.ConverterToModel

class DockerComposeToModel : ConverterToModel {
    private val containerToDatabase by lazy { hashMapOf<String, Database>() }

    override fun execute(specificTechnology: SpecificTechnology): ServiceBasedSystem {
        try {
            val dockerCompose = specificTechnology as DockerCompose
            val system = ServiceBasedSystem.create(dockerCompose.name)
            val mapServices = dockerCompose.services!!.filter { it.value.isService() }
            val mapDatabases = dockerCompose.services!!.filter { it.value.isDatabase() }
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
            dependsOn?.forEach { system.addDatabaseUsage(containerToDatabase[it]!!, service) }
        }

    private fun createDatabases(mapDatabases: Map<String, DockerContainer>, system: ServiceBasedSystem) =
        mapDatabases.forEach { (name, dbContainer) ->
            val dbModelAndMake = getDatabaseMakeAndModel(dbContainer.image!!) ?: return@forEach
            val (make, model) = dbModelAndMake
            val database = Database(name, make, model)
            system.addDatabase(database)
            containerToDatabase[name] = database
        }

    private fun getDatabaseMakeAndModel(image: String): Pair<String, String>? {
        val databaseImagesToMakeAndModel = listOf(
            "mongo" to ("MongoDB" to "NoSQL"),
            "postgres" to ("PostgreSQL" to "Relational"),
            "mysql" to ("MySQL" to "Relational"),
            "mariadb" to ("MariaDB" to "Relational"),
            "neo4j" to ("Neo4J" to "Graph-Oriented")
        )
        return databaseImagesToMakeAndModel.find { image.contains(it.first, true) }?.second
    }
}
