package domain.parsers

import domain.entities.platform_specific_model.PlatformSpecificModel
import domain.fetchers.Response

interface ParseData {
    fun run(response: Response): PlatformSpecificModel
}