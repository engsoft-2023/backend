package com.sortinghat.backend.data_collector.payloads

data class ServicesEndpointsRegistrationPayload(
    val repoUrl: String,
    val servicesAndOpenApiFilenames: List<ServiceAndOpenApi>
)

data class ServiceAndOpenApi(
    val serviceName: String,
    val openApiFilename: String
)
