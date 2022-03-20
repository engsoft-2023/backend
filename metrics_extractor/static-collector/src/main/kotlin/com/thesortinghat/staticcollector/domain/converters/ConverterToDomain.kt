package com.thesortinghat.staticcollector.domain.converters

import com.thesortinghat.staticcollector.domain.entities.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.entities.SpecificTechnology

interface ConverterToDomain {
    fun run(specificTechnology: SpecificTechnology): ServiceBasedSystem
}