package domain.usecases

import domain.converters.ConverterToPIM
import domain.entities.platform_independent_model.System
import domain.fetchers.FetchData
import domain.parsers.*
import domain.repositories.SystemRepository

class ExtractDataUseCase(
    private val fetchData: FetchData,
    private val parseData: ParseData,
    private val converterToPim: ConverterToPIM,
    private val systemRepository: SystemRepository,
) {

    fun run(url: String): System {
        val response = fetchData.run(url)
        val psm = parseData.run(response)
        val system = converterToPim.run(psm)
        persist(system)
        return system
    }

    private fun persist(system: System) {
        val sys = systemRepository.findByName(system.name)

        if (sys != null)
            throw Exception("system with this name already exists")

        systemRepository.save(system)
    }
}
