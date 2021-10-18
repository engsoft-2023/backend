package domain.entities.platform_specific_model.docker_compose

import domain.entities.base.Vertex

data class DockerContainer(val identifier: String) : Vertex() {
    var image: String = ""
    var build: String = ""
    var environment: HashMap<String, String> = hashMapOf()
    var dependsOn: DockerContainer? = null
}