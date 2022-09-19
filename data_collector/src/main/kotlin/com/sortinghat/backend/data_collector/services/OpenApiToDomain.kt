package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.OpenApi
import com.sortinghat.backend.data_collector.domain.SpecificTechnology
import com.sortinghat.backend.data_collector.exceptions.UnableToConvertDataException
import com.sortinghat.backend.domain.model.Operation
import org.springframework.stereotype.Service

@Service
class OpenApiToDomain : ConverterToDomain<Operation> {

    override fun execute(specificTechnology: SpecificTechnology): Set<Operation> {
        try {
            val openApi = specificTechnology as OpenApi
            val operations = mutableSetOf<Operation>()

            openApi.paths.forEach { (path, routeMap) ->
                val verbs = routeMap.keys

                verbs.forEach { verb ->
                    operations.add(Operation.fromString("$verb $path"))
                }
            }

            return operations
        } catch (e: Exception) {
            throw UnableToConvertDataException("unable to convert open api: ${e.message}")
        }
    }
}
