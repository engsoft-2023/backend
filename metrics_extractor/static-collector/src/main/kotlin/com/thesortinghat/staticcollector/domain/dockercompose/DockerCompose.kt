package com.thesortinghat.staticcollector.domain.dockercompose

import com.thesortinghat.staticcollector.domain.entities.SpecificTechnology

data class DockerCompose(
    var name: String = "",
    var version: String? = null,
    var services: Map<String, DockerContainer>? = null,
    var networks: Map<String, DockerNetwork>? = null
) : SpecificTechnology()
