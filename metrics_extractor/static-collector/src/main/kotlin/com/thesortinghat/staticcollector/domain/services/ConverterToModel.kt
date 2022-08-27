package com.thesortinghat.staticcollector.domain.services

import com.thesortinghat.staticcollector.domain.exceptions.UnableToParseDataException
import com.thesortinghat.staticcollector.domain.model.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.model.SpecificTechnology

interface ConverterToModel {

    @Throws(UnableToParseDataException::class)
    fun execute(specificTechnology: SpecificTechnology): ServiceBasedSystem
}
