package com.sortinghat.backend.metrics_extractor.services

import com.sortinghat.backend.metrics_extractor.vo.Extractions

interface ExtractSystemMetrics {
    fun execute(systemName: String): Extractions
}
