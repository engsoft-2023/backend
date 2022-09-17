package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.DockerCompose
import com.sortinghat.backend.data_collector.domain.SpecificTechnology
import com.sortinghat.backend.data_collector.exceptions.UnableToParseDataException
import com.sortinghat.backend.data_collector.utils.FetchResponse
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.representer.Representer

@Service
class ParseDockerCompose : DataParser {
    override fun execute(response: FetchResponse): SpecificTechnology {
        try {
            val representer = Representer()
            representer.propertyUtils.isSkipMissingProperties = true

            val yaml = Yaml(Constructor(DockerCompose::class.java), representer)
            val dockerCompose: DockerCompose = yaml.load(response.data)
            dockerCompose.name = response.systemName

            if (dockerCompose.services.isNullOrEmpty() || dockerCompose.services?.size == 0) {
                throw UnableToParseDataException("docker-compose file does not have containers")
            }

            return dockerCompose
        } catch (e: Exception) {
            throw UnableToParseDataException("unable to parse docker-compose file: ${e.message}")
        }
    }
}
