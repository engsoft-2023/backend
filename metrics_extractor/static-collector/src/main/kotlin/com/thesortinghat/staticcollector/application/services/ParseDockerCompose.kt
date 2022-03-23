package com.thesortinghat.staticcollector.application.services

import com.thesortinghat.staticcollector.domain.dockercompose.DockerCompose
import com.thesortinghat.staticcollector.domain.model.SpecificTechnology
import com.thesortinghat.staticcollector.domain.exceptions.UnableToParseDataException
import com.thesortinghat.staticcollector.domain.vo.FetchResponse
import com.thesortinghat.staticcollector.domain.services.DataParser
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
