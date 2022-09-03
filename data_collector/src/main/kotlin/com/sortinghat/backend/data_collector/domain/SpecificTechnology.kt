package com.sortinghat.backend.data_collector.domain

import java.util.UUID

abstract class SpecificTechnology {
    val id = UUID.randomUUID().toString()
}