package com.thesortinghat.staticcollector.domain.model

import java.util.UUID

abstract class SpecificTechnology {
    val id = UUID.randomUUID().toString()
}