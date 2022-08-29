package com.sortinghat.backend.metrics_extractor.domain.behaviors

interface MetricExtractor {
    fun getResult(): ExtractionResult

    fun getMetricDescription(): String
}
