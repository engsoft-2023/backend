package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.SpecificTechnology
import com.sortinghat.backend.data_collector.exceptions.UnableToParseDataException
import com.sortinghat.backend.domain.model.Service

interface ConverterToModel {

    @Throws(UnableToParseDataException::class)
    fun execute(specificTechnology: SpecificTechnology): Set<Service>
}
