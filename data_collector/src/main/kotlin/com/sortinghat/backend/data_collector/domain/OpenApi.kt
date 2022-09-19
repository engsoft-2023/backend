package com.sortinghat.backend.data_collector.domain

data class OpenApi(
    var openapi: Any? = null,
    var info: Any? = null,
    var servers: Any? = null,
    var paths: Map<String, Map<String, Any>> = mapOf()
) : SpecificTechnology()
