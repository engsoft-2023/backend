package domain.converters

import domain.entities.platform_specific_model.docker_compose.DockerComposeProject
import domain.entities.platform_specific_model.docker_compose.DockerContainer
import domain.entities.platform_specific_model.docker_compose.DockerNetwork
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DockerComposeConverterTest {
    private val dcProject = DockerComposeProject("my-project")

    @BeforeEach
    fun init() {
        val net = DockerNetwork("my-project_default")

        val db = DockerContainer("db")
        db.image = "mongodb:4.2"

        val app = DockerContainer("app")
        app.build = "."
        app.environment = hashMapOf("DB_HOST" to "db")
        app.dependsOn = db

        dcProject.addService(db)
        dcProject.addService(app)
        dcProject.addNetwork(net)
        dcProject.bindContainerToNetwork(app, net)
        dcProject.bindContainerToNetwork(db, net)
    }

    @Test
    @DisplayName("it returns a valid system for a given docker compose")
    fun testConvertingDockerProjectToSystem() {
        val converter = DockerComposeConverter()
        val system = converter.run(dcProject)
        val databases = system.databases()
        val services = system.services()
        val contexts = system.contexts()
        val ctxDbLinks = system.ctxDbLinks()
        val ctxServiceLinks = system.ctxServiceLinks()
        val dbServiceLinks = system.dbServiceLinks()

        assertEquals(1, databases.size)
        assertEquals("db", databases[0].name)
        assertEquals(1, services.size)
        assertEquals("app", services[0].name)
        assertEquals(1, contexts.size)
        assertEquals("my-project_default", contexts[0].name)
        assertEquals(1, ctxDbLinks.size)
        assertEquals(1, ctxServiceLinks.size)
        assertEquals(1, dbServiceLinks.size)
    }
}