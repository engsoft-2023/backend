package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.SpecificTechnology
import com.sortinghat.backend.data_collector.exceptions.UnableToConvertDataException

interface ConverterToDomain<T> {

    @Throws(UnableToConvertDataException::class)
    fun execute(specificTechnology: SpecificTechnology): Set<T>
}
