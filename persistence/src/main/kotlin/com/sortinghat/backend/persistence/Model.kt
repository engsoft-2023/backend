package com.sortinghat.backend.persistence

import com.sortinghat.backend.domain.model.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ServiceSchema(
    @Id val name: String,
    val responsibility: String,
    val module: Module,
    val system: ServiceBasedSystem,
    val databasesUsages: Set<DbSchema> = mutableSetOf(),
    val exposedOperations: Set<Operation> = mutableSetOf(),
    val consumedOperations: Set<Operation> = mutableSetOf(),
    val channelsPublishing: Set<MessageChannel> = mutableSetOf(),
    val channelsSubscribing: Set<MessageChannel> = mutableSetOf()
) {
    companion object {
        fun toSchema(s: Service) =
            ServiceSchema(
                name = s.name,
                responsibility = s.responsibility,
                module = s.module,
                system = s.system,
                databasesUsages = s.databasesUsages.map { db -> DbSchema.toSchema(db) }.toSet(),
                exposedOperations = s.exposedOperations,
                consumedOperations = s.consumedOperations,
                channelsPublishing = s.channelsPublishing,
                channelsSubscribing = s.channelsSubscribing
            )
    }
}

data class DbSchema(
    val namespace: String,
    val model: DataSource,
    val usedBy: Map<String, DatabaseAccessType> = mutableMapOf()
) {
    companion object {
        fun toSchema(db: Database) =
            DbSchema(
                namespace = db.namespace,
                model = db.model,
                usedBy = db.usages().associate { it.name to db.getAccessType(it)!! }
            )
    }
}
