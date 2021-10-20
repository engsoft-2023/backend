package domain.entities.platform_specific_model.docker_compose

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DockerComposeProjectTest {

    @Test
    @DisplayName("create a docker compose project should work")
    fun testDockerComposeProject() {
        val dc = DockerComposeProject("docker-compose.yaml")
        val container = DockerContainer("web")
        val network = DockerNetwork("project_default")

        dc.bindContainerToNetwork(container, network)

        assertEquals(1, dc.networks().size)
        assertEquals(1, dc.containers().size)
        assertEquals(container, dc.containers()[0])
        assertEquals(network, dc.networks()[0])
        assertEquals(1, dc.getAllNetworkContainerLinks().size)
        assertEquals(container, dc.getAllNetworkContainerLinks()[0].container)
        assertEquals(network, dc.getAllNetworkContainerLinks()[0].network)
    }

    @Test
    @DisplayName("check when a container represents a database")
    fun testWhenContainerIsDatabase() {
        val container = DockerContainer("db")
        container.image = "mongodb:4.2"
        assertTrue(ContainerDecider.isDatabase(container))
    }

    @Test
    @DisplayName("check when a container doesn't represent a database")
    fun testWhenContainerIsNotDatabase() {
        val container = DockerContainer("db")
        assertFalse(ContainerDecider.isDatabase(container))
    }

    @Test
    @DisplayName("check when a container represents a service")
    fun testWhenContainerIsService() {
        val container = DockerContainer("app")
        container.build = "."
        assertTrue(ContainerDecider.isService(container))
    }
}