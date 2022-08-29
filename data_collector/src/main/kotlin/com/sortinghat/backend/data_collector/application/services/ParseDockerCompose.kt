package com.sortinghat.backend.data_collector.application.services

import com.sortinghat.backend.data_collector.domain.dockercompose.DockerCompose
import com.sortinghat.backend.data_collector.domain.model.SpecificTechnology
import com.sortinghat.backend.data_collector.domain.exceptions.UnableToParseDataException
import com.sortinghat.backend.data_collector.domain.vo.FetchResponse
import com.sortinghat.backend.data_collector.domain.services.DataParser
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

@Service
class ParseDockerCompose : DataParser {
    override fun execute(response: FetchResponse): SpecificTechnology {
        try {
            val yaml = Yaml(Constructor(DockerCompose::class.java))
            val dockerCompose: DockerCompose = yaml.load(response.data)
            dockerCompose.name = response.systemName
            return dockerCompose
        } catch (e: Exception) {
            throw UnableToParseDataException("unable to parse docker-compose file: ${e.message}")
        }
    }
}
