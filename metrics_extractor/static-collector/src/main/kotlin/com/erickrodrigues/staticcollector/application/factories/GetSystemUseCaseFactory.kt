package com.erickrodrigues.staticcollector.application.factories

import com.erickrodrigues.staticcollector.domain.ports.ServiceBasedSystemRepository
import com.erickrodrigues.staticcollector.domain.services.GetSystemData
import org.springframework.stereotype.Component

@Component
class GetSystemUseCaseFactory(private val repo: ServiceBasedSystemRepository) {

    fun create() = GetSystemData(repo)
}
