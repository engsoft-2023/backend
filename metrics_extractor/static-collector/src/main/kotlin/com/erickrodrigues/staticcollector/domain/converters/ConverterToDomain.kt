package com.erickrodrigues.staticcollector.domain.converters

import com.erickrodrigues.staticcollector.domain.entities.ServiceBasedSystem
import com.erickrodrigues.staticcollector.domain.entities.SpecificTechnology

interface ConverterToDomain {
    fun run(specificTechnology: SpecificTechnology): ServiceBasedSystem
}