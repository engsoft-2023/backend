package com.sortinghat.backend.data_collector.domain.services

import com.sortinghat.backend.data_collector.domain.exceptions.UnableToParseDataException
import com.sortinghat.backend.data_collector.domain.model.SpecificTechnology
import com.sortinghat.backend.domain.model.Service

interface ConverterToModel {

    @Throws(UnableToParseDataException::class)
    fun execute(specificTechnology: SpecificTechnology): Set<Service>
}
