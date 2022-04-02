package com.thesortinghat.staticcollector.domain.model

import com.thesortinghat.staticcollector.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Service(@Serializable(with = UUIDSerializer::class) val id: UUID, val name: String) {
    constructor(name: String): this(UUID.randomUUID(), name)

    override fun equals(other: Any?): Boolean {
        return other is Service && other.id == this.id
    }

    override fun hashCode() = id.hashCode()
}
