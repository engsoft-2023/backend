package domain.entities.platform_specific_model.docker_compose

import domain.entities.base.Graph
import domain.entities.platform_specific_model.PlatformSpecificModel

data class DockerComposeProject(val name: String) : PlatformSpecificModel() {
    private val graph: Graph = Graph()

    fun addContainer(container: DockerContainer) {
        graph.addVertex(container)
    }

    fun addNetwork(network: DockerNetwork) {
        graph.addVertex(network)
    }

    fun bindContainerToNetwork(container: DockerContainer, network: DockerNetwork) {
        addContainer(container)
        addNetwork(network)
        graph.addEdge(NetContainerEdge(network, container))
    }

    fun containers(): List<DockerContainer> =
        graph.vertices.filterIsInstance(DockerContainer::class.java)

    fun networks(): List<DockerNetwork> =
        graph.vertices.filterIsInstance(DockerNetwork::class.java)

    fun getAllNetworkContainerLinks(): List<NetContainerEdge> =
        graph.edges().filterIsInstance(NetContainerEdge::class.java)
}
