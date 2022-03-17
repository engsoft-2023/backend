package com.erickrodrigues.staticcollector.domain.dockercompose

import com.erickrodrigues.staticcollector.domain.entities.SpecificTechnology

data class DockerCompose(
    var name: String = "",
    var version: String? = null,
    var services: Map<String, DockerContainer>? = null,
    var networks: Map<String, DockerNetwork>? = null
) : SpecificTechnology()
