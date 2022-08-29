package com.sortinghat.backend.metrics_extractor.domain.services.data_coupling_dimension

import com.sortinghat.backend.domain.behaviors.Visitor
import com.sortinghat.backend.domain.behaviors.VisitorBag
import com.sortinghat.backend.domain.model.Database
import com.sortinghat.backend.domain.model.DatabaseAccessType
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.metrics_extractor.domain.behaviors.*

class DatabasesByAccessTypeMetric(
    private val accessType: DatabaseAccessType,
    private val visitorBag: VisitorBag = VisitorBag()
) : MetricExtractor, Visitor by visitorBag {

    private val services = mutableSetOf<Service>()
    private val descriptionMap = DatabaseAccessType.values().associateWith {
        "Number of data sources where each component performs $it operations"
    }

    override fun getResult(): PerComponentResult {
        val servicesResult = services.associateWith { service ->
            service.databasesUsages
                .filter { db -> db.getAccessType(service) == accessType }
                .size
        }

        val modulesResult = services
            .groupBy { s -> s.module }
            .mapValues {
                it.value
                    .fold(setOf<Database>()) { acc, service ->
                        acc.plus(service.databasesUsages.filter { db -> db.getAccessType(service) == accessType })
                    }
                    .size
            }

        return PerComponentResult(
            modules = modulesResult.mapKeys { it.key.name },
            services = servicesResult.mapKeys { it.key.name }
        )
    }

    override fun getMetricDescription() = descriptionMap[accessType]!!

    override fun visit(s: Service) {
        if (s in visitorBag.visited) return

        visitorBag.addVisited(s)
        services.add(s)
        s.children().forEach { it.accept(this) }
    }
}
