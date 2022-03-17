package com.erickrodrigues.staticcollector.application.factories

import com.erickrodrigues.staticcollector.domain.ports.ServiceBasedSystemRepository
import com.erickrodrigues.staticcollector.domain.usecases.GetSystemDataUseCase
import org.springframework.stereotype.Component

@Component
class GetSystemUseCaseFactory(private val repo: ServiceBasedSystemRepository) {

    fun create() = GetSystemDataUseCase(repo)
}
