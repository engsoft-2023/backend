package com.thesortinghat.staticcollector.application.factories

import com.thesortinghat.staticcollector.domain.ports.ServiceBasedSystemRepository
import com.thesortinghat.staticcollector.domain.services.GetSystemData
import org.springframework.stereotype.Component

@Component
class GetSystemUseCaseFactory(private val repo: ServiceBasedSystemRepository) {

    fun create() = GetSystemData(repo)
}
