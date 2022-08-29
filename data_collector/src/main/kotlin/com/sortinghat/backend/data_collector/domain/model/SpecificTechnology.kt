package com.sortinghat.backend.data_collector.domain.model

import java.util.UUID

abstract class SpecificTechnology {
    val id = UUID.randomUUID().toString()
}