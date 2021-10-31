package com.sortinghat.static_collector.domain.entities.platform_specific_model.docker_compose

import com.sortinghat.static_collector.domain.entities.base.Edge

data class NetContainerEdge(
    val network: DockerNetwork,
    val container: DockerContainer
) : Edge(network, container)
