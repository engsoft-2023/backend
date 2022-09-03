package com.sortinghat.backend.metrics_extractor.services.size_dimension

import com.sortinghat.backend.domain.behaviors.Visitor
import com.sortinghat.backend.domain.behaviors.VisitorBag
import com.sortinghat.backend.domain.model.Module
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.metrics_extractor.services.MetricExtractor
import com.sortinghat.backend.metrics_extractor.vo.ValueResult

class SystemComponentsMetric(
    private val visitorBag: VisitorBag = VisitorBag()
) : MetricExtractor, Visitor by visitorBag {

    private var numberOfModules = 0
    private var numberOfServices = 0

    override fun getResult(): ValueResult {
        val modulesString = if (numberOfModules == 1) "$numberOfModules module" else "$numberOfModules modules"
        val servicesString = if (numberOfServices == 1) "$numberOfServices service" else "$numberOfServices services"
        return ValueResult(value = "$servicesString and $modulesString")
    }

    override fun getMetricDescription(): String {
        return "Number of system components"
    }

    override fun visit(s: Service) {
        if (s in visitorBag.visited) return

        visitorBag.addVisited(s)
        numberOfServices += 1
        s.children().forEach { it.accept(this) }
    }

    override fun visit(module: Module) {
        if (module in visitorBag.visited) return

        visitorBag.addVisited(module)
        numberOfModules += 1
        module.children().forEach { it.accept(this) }
    }
}
