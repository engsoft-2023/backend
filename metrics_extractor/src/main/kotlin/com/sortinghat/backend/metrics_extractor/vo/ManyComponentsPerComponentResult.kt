package com.sortinghat.backend.metrics_extractor.vo

data class ManyComponentsPerComponentResult(
    val modules: Map<String, Map<String, Int>>,
    val services: Map<String, Map<String, Int>>
) : ExtractionResult
