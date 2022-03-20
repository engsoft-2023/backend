package com.thesortinghat.staticcollector.application.adapters

import com.thesortinghat.staticcollector.domain.dockercompose.DockerCompose
import com.thesortinghat.staticcollector.domain.entities.SpecificTechnology
import com.thesortinghat.staticcollector.domain.exceptions.UnableToParseDataException
import com.thesortinghat.staticcollector.domain.vo.FetchResponse
import com.thesortinghat.staticcollector.domain.ports.DataParserPort
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

class DockerComposeParserAdapter : DataParserPort {
    override fun run(response: FetchResponse): SpecificTechnology {
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
