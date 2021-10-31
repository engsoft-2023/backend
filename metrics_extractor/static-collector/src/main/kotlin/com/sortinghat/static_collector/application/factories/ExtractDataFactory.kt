package com.sortinghat.static_collector.application.factories

import com.sortinghat.static_collector.domain.usecases.ExtractDataUseCase

interface ExtractDataFactory {
    fun create(): ExtractDataUseCase
}