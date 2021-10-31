package com.sortinghat.static_collector.application.yaml

import com.sortinghat.static_collector.domain.entities.platform_specific_model.PlatformSpecificModel
import com.sortinghat.static_collector.domain.entities.platform_specific_model.docker_compose.DockerComposeProject
import com.sortinghat.static_collector.domain.entities.platform_specific_model.docker_compose.DockerContainer
import com.sortinghat.static_collector.domain.entities.platform_specific_model.docker_compose.DockerNetwork
import com.sortinghat.static_collector.domain.exceptions.UnableToParseDataException
import com.sortinghat.static_collector.domain.ports.ParseData
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.error.YAMLException

class YAMLParser : ParseData {
    private val mapContainerToDependsOn by lazy { hashMapOf<DockerContainer, List<String>>() }
    private val mapNameToContainer by lazy { hashMapOf<String, DockerContainer>() }

    override fun run(data: String): PlatformSpecificModel {
        val yaml = Yaml()
        val dockerCompose: HashMap<String, Any> = yaml.load(data)

        if (dockerCompose["services"] == null) {
            throw YAMLException("File without containers")
        }

        try {
            val dockerProject = DockerComposeProject("project")
            createContainers(dockerProject, dockerCompose)
            createNetworks(dockerProject)
            mapDependsOn()

            return dockerProject
        } catch (e: Exception) {
            throw UnableToParseDataException("Failed while parsing docker-compose data")
        }
    }

    private fun createContainers(dockerProject: DockerComposeProject, dockerCompose: HashMap<String, Any>) {
        val services = dockerCompose["services"] as HashMap<*, *>

        services.keys.forEach { key ->
            val identifier = key.toString()
            val container = DockerContainer(identifier)
            val containerProperties = services[key] as HashMap<*, *>
            val image = containerProperties["image"]
            val build = containerProperties["build"]
            val environment = containerProperties["environment"]
            val dependsOn = containerProperties["depends_on"]

            container.image = image.toString()
            container.build = build.toString()

            createContainerEnvironment(container, environment)

            if (dependsOn != null) {
                mapContainerToDependsOn[container] = (dependsOn as List<*>).map { value -> value.toString() }
            }

            mapNameToContainer[identifier] = container
            dockerProject.addContainer(container)
        }
    }

    private fun createNetworks(dockerProject: DockerComposeProject) {
        // for a while, creating a unique network for all containers
        val network = DockerNetwork("default")
        dockerProject.addNetwork(network)
        dockerProject.containers().forEach { c -> dockerProject.bindContainerToNetwork(c, network) }
    }

    private fun mapDependsOn() {
        mapContainerToDependsOn.forEach { (container, dependencies) ->
            dependencies.forEach { dep ->
                val dependOn = mapNameToContainer[dep]!!
                container.dependsOn.add(dependOn)
            }
        }
    }

    private fun createContainerEnvironment(container: DockerContainer, environment: Any?) {
        if (environment != null) {
            val hash = hashMapOf<String, String>()
            (environment as List<*>).forEach { env ->
                val pair = env.toString().split("=")
                val k = pair[0]
                val v = pair[1]
                hash[k] = v
            }

            container.environment = hash
        }
    }
}