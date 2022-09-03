package com.sortinghat.backend.metrics_extractor.services

import com.sortinghat.backend.metrics_extractor.vo.ExtractionResult

interface MetricExtractor {
    fun getResult(): ExtractionResult

    fun getMetricDescription(): String
}
