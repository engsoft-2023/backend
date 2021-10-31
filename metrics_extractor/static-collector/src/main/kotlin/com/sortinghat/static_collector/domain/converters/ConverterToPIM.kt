package com.sortinghat.static_collector.domain.converters

import com.sortinghat.static_collector.domain.entities.platform_independent_model.System
import com.sortinghat.static_collector.domain.entities.platform_specific_model.PlatformSpecificModel

interface ConverterToPIM {
    fun run(platformSpecificModel: PlatformSpecificModel): System
}