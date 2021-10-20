package domain.converters

import domain.entities.platform_independent_model.Context
import domain.entities.platform_independent_model.Database
import domain.entities.platform_independent_model.Service
import domain.entities.platform_independent_model.System
import domain.entities.platform_specific_model.PlatformSpecificModel
import domain.entities.platform_specific_model.docker_compose.ContainerDecider
import domain.entities.platform_specific_model.docker_compose.DockerComposeProject
import domain.entities.platform_specific_model.docker_compose.DockerContainer
import domain.entities.platform_specific_model.docker_compose.DockerNetwork

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
            throw Exception("Unable to convert the docker compose project")
        }
    }

    private fun convertContainersToServicesAndDatabases(dcProject: DockerComposeProject, system: System) {
        dcProject.services().forEach { container ->
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
            if (container.dependsOn == null) return

            val dependsOn = container.dependsOn!!
            val db = linksContainersToDatabases[dependsOn] ?: return
            system.bindDatabaseToService(db, service)
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