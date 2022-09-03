package com.sortinghat.backend.metrics_extractor.services.sync_coupling_dimension

import com.sortinghat.backend.domain.behaviors.Visitor
import com.sortinghat.backend.domain.behaviors.VisitorBag
import com.sortinghat.backend.domain.model.Operation
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.metrics_extractor.services.MetricExtractor
import com.sortinghat.backend.metrics_extractor.vo.PerComponentResult

class OperationsInvokedMetric(
    private val visitorBag: VisitorBag = VisitorBag()
) : MetricExtractor, Visitor by visitorBag {

    private val serviceToOperations = hashMapOf<Service, Set<Operation>>()
    private val operationToServices = hashMapOf<Operation, Set<Service>>()

    override fun getResult(): PerComponentResult {
        val servicesResult = serviceToOperations.mapValues { (_, operations) ->
            operations
                .filter { operation -> operationToServices.getOrDefault(operation, setOf()).isNotEmpty() }
                .size
        }
        val modulesResult = serviceToOperations
            .keys
            .groupBy { service -> service.module }
            .mapValues { (_, services) ->
                services.fold(setOf<Operation>()) { acc, service ->
                    acc.plus(
                        serviceToOperations.getOrDefault(service, setOf())
                    )
                }
            }
            .mapValues { (module, operations) ->
                operations
                    .filter { operation ->
                        operationToServices
                            .getOrDefault(operation, setOf()).any { service -> service.module != module }
                    }
                    .size
            }

        return PerComponentResult(
            modules = modulesResult.mapKeys { it.key.name },
            services = servicesResult.mapKeys { it.key.name },
        )
    }

    override fun getMetricDescription(): String {
        return "Number of different operations invoked from other components"
    }

    override fun visit(s: Service) {
        if (s in visitorBag.visited) return

        visitorBag.addVisited(s)
        serviceToOperations[s] = s.exposedOperations
        s.consumedOperations.forEach { operation ->
            operationToServices.merge(operation, setOf(s)) { old, new -> old.plus(new) }
        }
        s.children().forEach { it.accept(this) }
    }
}
