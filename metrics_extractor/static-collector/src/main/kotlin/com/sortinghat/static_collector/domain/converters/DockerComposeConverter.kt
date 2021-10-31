package com.sortinghat.static_collector.domain.converters

import com.sortinghat.static_collector.domain.entities.platform_independent_model.Context
import com.sortinghat.static_collector.domain.entities.platform_independent_model.Database
import com.sortinghat.static_collector.domain.entities.platform_independent_model.Service
import com.sortinghat.static_collector.domain.entities.platform_independent_model.System
import com.sortinghat.static_collector.domain.entities.platform_specific_model.PlatformSpecificModel
import com.sortinghat.static_collector.domain.entities.platform_specific_model.docker_compose.ContainerDecider
import com.sortinghat.static_collector.domain.entities.platform_specific_model.docker_compose.DockerComposeProject
import com.sortinghat.static_collector.domain.entities.platform_specific_model.docker_compose.DockerContainer
import com.sortinghat.static_collector.domain.entities.platform_specific_model.docker_compose.DockerNetwork
import com.sortinghat.static_collector.domain.exceptions.UnableToConvertException

class DockerComposeConverter : ConverterToPIM {
    private val linksContainersToServices by lazy { hashMapOf<DockerContainer, Service>() }
    private val linksContainersToDatabases by lazy { hashMapOf<DockerContainer, Database>() }
    private val linksNetworksToContexts by lazy { hashMapOf<DockerNetwork, Context>() }

    override fun run(platformSpecificModel: PlatformSpecificModel): System {
        try {
            val dcProject = platformSpecificModel as DockerComposeProject
            val system = System(dcProject.name)

            convertContainersToServicesAndDatabases(dcProject, system)
            convertNetworksToContexts(dcProject, system)
            mapServicesAndDatabasesToTheirContexts(dcProject, system)
            createServiceDbLinks(system)

            return system
        } catch (ex: Exception) {
            throw UnableToConvertException("Unable to convert the docker compose project")
        }
    }

    private fun convertContainersToServicesAndDatabases(dcProject: DockerComposeProject, system: System) {
        dcProject.containers().forEach { container ->
            if (ContainerDecider.isDatabase(container)) {
                val database = createDatabase(container)
                linksContainersToDatabases[container] = database
                system.addDatabase(database)
            } else {
                val service = createService(container)
                linksContainersToServices[container] = service
                system.addService(service)
            }
        }
    }

    private fun convertNetworksToContexts(dcProject: DockerComposeProject, system: System) {
        dcProject.networks().forEach { network ->
            val context = createContext(network)
            linksNetworksToContexts[network] = context
            system.addContext(context)
        }
    }

    private fun mapServicesAndDatabasesToTheirContexts(dcProject: DockerComposeProject, system: System) {
        dcProject.getAllNetworkContainerLinks().forEach { (network, container) ->
            val context = linksNetworksToContexts[network]!!
            val database = linksContainersToDatabases[container]

            if (database == null) {
                val service = linksContainersToServices[container]!!
                system.bindServiceToContext(service, context)
            } else {
                system.bindDatabaseToContext(database, context)
            }
        }
    }

    private fun createServiceDbLinks(system: System) {
        linksContainersToServices.forEach { (container, service) ->
            if (container.dependsOn.isEmpty()) return

            container.dependsOn.forEach { c ->
                val db = linksContainersToDatabases[c] ?: return
                system.bindDatabaseToService(db, service)
            }
        }
    }

    private fun createDatabase(container: DockerContainer): Database {
        val dbModelAndMake = ContainerDecider.getDatabaseMakeAndModel(container.image)
        val (make, model) = dbModelAndMake!!
        val name = container.identifier
        return Database(name, make, model)
    }

    private fun createService(container: DockerContainer) = Service(container.identifier)

    private fun createContext(network: DockerNetwork) = Context(network.identifier)
}