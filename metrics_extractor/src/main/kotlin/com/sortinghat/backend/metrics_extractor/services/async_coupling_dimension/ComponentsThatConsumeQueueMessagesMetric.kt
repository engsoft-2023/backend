package com.sortinghat.backend.metrics_extractor.services.async_coupling_dimension

import com.sortinghat.backend.domain.behaviors.Visitor
import com.sortinghat.backend.domain.behaviors.VisitorBag
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.metrics_extractor.services.MetricExtractor
import com.sortinghat.backend.metrics_extractor.vo.ValueResultPerComponentType

class ComponentsThatConsumeQueueMessagesMetric(
    private val visitorBag: VisitorBag = VisitorBag()
) : MetricExtractor, Visitor by visitorBag {

    private val servicesThatConsumeMessages = hashSetOf<Service>()

    override fun getResult(): ValueResultPerComponentType {
        return ValueResultPerComponentType(
            services = servicesThatConsumeMessages.size,
            modules = servicesThatConsumeMessages.groupBy { s -> s.module }.keys.size
        )
    }

    override fun getMetricDescription(): String {
        return "Number of components that consume messages from the queue"
    }

    override fun visit(s: Service) {
        if (s in visitorBag.visited) return

        visitorBag.addVisited(s)
        if (s.channelsSubscribing.size > 0) servicesThatConsumeMessages.add(s)
        s.children().forEach { it.accept(this) }
    }
}
