package com.thesortinghat.staticcollector.domain.entities

import java.util.UUID

abstract class SpecificTechnology {
    val id = UUID.randomUUID().toString()
}