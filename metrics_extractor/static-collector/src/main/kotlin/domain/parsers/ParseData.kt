package domain.parsers

import domain.entities.platform_specific_model.PlatformSpecificModel

interface ParseData {
    fun run(data: String): PlatformSpecificModel
}