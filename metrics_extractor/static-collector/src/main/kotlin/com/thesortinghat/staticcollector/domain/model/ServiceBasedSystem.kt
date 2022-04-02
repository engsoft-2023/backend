package com.thesortinghat.staticcollector.domain.model

import com.thesortinghat.staticcollector.domain.exceptions.DuplicateSystemComponentException
import com.thesortinghat.staticcollector.domain.vo.Connection
import com.thesortinghat.staticcollector.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class ServiceBasedSystem private constructor(
        @Serializable(with = UUIDSerializer::class) val id: UUID,
        val name: String,
) {
    companion object {
        fun create(name: String): ServiceBasedSystem {
            if (name.isBlank()) throw IllegalArgumentException("system's name cannot be blank")
            return ServiceBasedSystem(name)
        }
    }

    private val services = hashSetOf<Service>()
    private val databases = hashSetOf<Database>()
    private val databasesUsages = hashSetOf<DatabaseUsage>()
    @Transient private val serviceNames = hashSetOf<String>()
    @Transient private val databaseNames = hashSetOf<String>()

    private constructor(name: String) : this(UUID.randomUUID(), name)

    private constructor(
            id: UUID,
            name: String,
            services: List<Service>,
            databases: List<Database>,
            databasesUsages: List<DatabaseUsage>
    ) : this(id, name) {
        this.services.addAll(services)
        this.databases.addAll(databases)
        this.databasesUsages.addAll(databasesUsages)
        this.serviceNames.addAll(this.services.map { it.name })
        this.databaseNames.addAll(this.databases.map { it.name })
    }

    fun addService(s: Service) {
        if (s in services || s.name in serviceNames)
            throw DuplicateSystemComponentException("duplicate service in the system")

        services.add(s)
        serviceNames.add(s.name)
    }

    fun addDatabase(db: Database) {
        if (db in databases || db.name in databaseNames)
            throw DuplicateSystemComponentException("duplicate database in the system: ${db.name}")

        databases.add(db)
        databaseNames.add(db.name)
    }

    fun addDatabaseUsage(db: Database, s: Service, payload: Connection? = null) {
        if (db !in databases && db.name !in databaseNames) addDatabase(db)
        if (s !in services && s.name !in serviceNames) addService(s)
        val usage = DatabaseUsage(db.id, s.id, payload)
        if (usage in databasesUsages) throw DuplicateSystemComponentException("duplicate database usage in the system")
        databasesUsages.add(usage)
    }

    fun services() = services.toList()

    fun databases() = databases.toList()

    fun databasesUsages() = databasesUsages.toList()
}
