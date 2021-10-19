package domain.conversors

import domain.entities.platform_independent_model.System
import domain.entities.platform_specific_model.PlatformSpecificModel

interface ConverterToPIM {
    fun run(platformSpecificModel: PlatformSpecificModel): System
}