package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.DockerCompose
import com.sortinghat.backend.data_collector.domain.DockerContainer
import com.sortinghat.backend.data_collector.domain.SpecificTechnology
import com.sortinghat.backend.data_collector.exceptions.UnableToConvertDataException
import com.sortinghat.backend.domain.model.*

@org.springframework.stereotype.Service
class DockerComposeToDomain : ConverterToDomain {
    private val containerToDatabase by lazy { hashMapOf<String, Database>() }

    override fun execute(specificTechnology: SpecificTechnology): Set<Service> {
        try {
            val dockerCompose = specificTechnology as DockerCompose
            val system = ServiceBasedSystem(dockerCompose.name, "")
            val mapServices = dockerCompose.services!!.filter { it.value.isService() }
            val mapDatabases = dockerCompose.services!!.filter { it.value.isDatabase() }
            createDatabases(mapDatabases)

            return createServices(mapServices, system)
        } catch (e: Exception) {
            throw UnableToConvertDataException("unable to convert docker-compose to a service-based system: ${e.message}")
        }
    }

    private fun createServices(mapServices: Map<String, DockerContainer>, system: ServiceBasedSystem): Set<Service> {
        val services = mutableSetOf<Service>()

        mapServices.forEach { (name, serviceContainer) ->
            val service = Service(name, "", Module(name), system)
            val dependsOn = serviceContainer.depends_on
            dependsOn?.forEach { service.addUsage(containerToDatabase[it]!!, DatabaseAccessType.ReadWrite) }

            services.add(service)
        }

        return services
    }

    private fun createDatabases(mapDatabases: Map<String, DockerContainer>) =
        mapDatabases.forEach { (name, dbContainer) ->
            val dataSource = getDataSourceFromImage(dbContainer.image!!) ?: return@forEach
            val database = Database(name, dataSource)
            containerToDatabase[name] = database
        }

    private fun getDataSourceFromImage(image: String): DataSource? {
        val imagesToDataSources = listOf(
            "mongo" to DataSource.MongoDb,
            "postgres" to DataSource.Postgres,
            "mysql" to DataSource.MySql,
            "mariadb" to DataSource.MariaDb,
            "neo4j" to DataSource.Neo4j
        )
        return imagesToDataSources.find { image.contains(it.first, true) }?.second
    }
}
