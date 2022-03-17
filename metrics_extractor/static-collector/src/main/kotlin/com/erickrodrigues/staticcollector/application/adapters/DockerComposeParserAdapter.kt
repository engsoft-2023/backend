package com.erickrodrigues.staticcollector.application.adapters

import com.erickrodrigues.staticcollector.domain.dockercompose.DockerCompose
import com.erickrodrigues.staticcollector.domain.entities.SpecificTechnology
import com.erickrodrigues.staticcollector.domain.exceptions.UnableToParseDataException
import com.erickrodrigues.staticcollector.domain.vo.FetchResponse
import com.erickrodrigues.staticcollector.domain.ports.DataParserPort
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
