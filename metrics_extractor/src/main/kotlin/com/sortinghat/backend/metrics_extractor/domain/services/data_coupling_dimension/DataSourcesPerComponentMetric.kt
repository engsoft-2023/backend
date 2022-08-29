package com.sortinghat.backend.metrics_extractor.domain.services.data_coupling_dimension

import com.sortinghat.backend.domain.behaviors.Visitor
import com.sortinghat.backend.domain.behaviors.VisitorBag
import com.sortinghat.backend.domain.model.Database
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.metrics_extractor.domain.behaviors.*

class DataSourcesPerComponentMetric(
    private val visitorBag: VisitorBag = VisitorBag()
) : MetricExtractor, Visitor by visitorBag {

    private val usages = mutableMapOf<Service, Set<Database>>()

    override fun getResult(): PerComponentResult {
        val servicesResult = usages.mapValues { it.value.size }
        val modulesResult = servicesResult.keys
            .groupBy { it.module }
            .mapValues { it.value.fold(setOf<Database>()) { acc, service -> acc.plus(service.databasesUsages) }.size }

        return PerComponentResult(
            modules = modulesResult.mapKeys { it.key.name },
            services = servicesResult.mapKeys { it.key.name }
        )
    }

    override fun getMetricDescription(): String {
        return "Number of data sources per component"
    }

    override fun visit(s: Service) {
        if (s in visitorBag.visited) return

        visitorBag.addVisited(s)
        usages[s] = s.databasesUsages
        s.children().forEach { it.accept(this) }
    }
}
