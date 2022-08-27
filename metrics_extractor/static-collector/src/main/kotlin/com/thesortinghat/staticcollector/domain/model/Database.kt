package com.thesortinghat.staticcollector.domain.model

import com.thesortinghat.staticcollector.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Database(
        @Serializable(with = UUIDSerializer::class) val id: UUID,
        val name: String,
        val make: String,
        val model: String
) {
    constructor(name: String, make: String, model: String): this(UUID.randomUUID(), name, make, model)

    override fun equals(other: Any?): Boolean {
        return other is Database && other.id == this.id
    }

    override fun hashCode() = id.hashCode()
}
