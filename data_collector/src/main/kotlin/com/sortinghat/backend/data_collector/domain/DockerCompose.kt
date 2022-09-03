package com.sortinghat.backend.data_collector.domain

data class DockerCompose(
    var name: String = "",
    var version: String? = null,
    var services: Map<String, DockerContainer>? = null,
    var networks: Map<String, DockerNetwork>? = null
) : SpecificTechnology()
