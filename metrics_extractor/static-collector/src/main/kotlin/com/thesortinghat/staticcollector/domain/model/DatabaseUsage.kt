package com.thesortinghat.staticcollector.domain.model

import com.thesortinghat.staticcollector.domain.vo.Connection
import com.thesortinghat.staticcollector.utils.UUIDSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class DatabaseUsage(
        @Serializable(with = UUIDSerializer::class) val dbId: UUID,
        @Serializable(with = UUIDSerializer::class) val serviceId: UUID,
        @Required val payload: Connection? = null
) {
    override fun equals(other: Any?): Boolean {
        return other is DatabaseUsage && (dbId == other.dbId && serviceId == other.serviceId)
    }

    override fun hashCode(): Int {
        var result = dbId.hashCode()
        result = 31 * result + serviceId.hashCode()
        return result
    }
}
