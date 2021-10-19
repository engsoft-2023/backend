package domain.converters

import domain.entities.platform_independent_model.System
import domain.entities.platform_specific_model.PlatformSpecificModel
import domain.entities.platform_specific_model.docker_compose.DockerComposeProject

class DockerComposeConverter : ConverterToPIM {

    override fun run(platformSpecificModel: PlatformSpecificModel): System {
        // simple implementation
        lateinit var dcProject: DockerComposeProject

        try {
            dcProject = platformSpecificModel as DockerComposeProject
        } catch (ex: Exception) {
            throw Exception("It wasn't possible to convert a docker compose project")
        }
        return System(dcProject.name)
    }
}