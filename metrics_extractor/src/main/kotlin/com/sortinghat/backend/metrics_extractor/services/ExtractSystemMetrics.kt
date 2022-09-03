package com.sortinghat.backend.metrics_extractor.services

import com.sortinghat.backend.domain.model.DatabaseAccessType
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.metrics_extractor.vo.Extractions
import com.sortinghat.backend.metrics_extractor.services.async_coupling_dimension.*
import com.sortinghat.backend.metrics_extractor.services.data_coupling_dimension.DataSourcesPerComponentMetric
import com.sortinghat.backend.metrics_extractor.services.data_coupling_dimension.DatabasesByAccessTypeMetric
import com.sortinghat.backend.metrics_extractor.services.data_coupling_dimension.SharedDatabasesMetric
import com.sortinghat.backend.metrics_extractor.services.size_dimension.*
import com.sortinghat.backend.metrics_extractor.services.sync_coupling_dimension.ClientsThatInvokeOperationsMetric
import com.sortinghat.backend.metrics_extractor.services.sync_coupling_dimension.ComponentsThatHaveOperationsInvokedMetric
import com.sortinghat.backend.metrics_extractor.services.sync_coupling_dimension.OperationsInvokedByEachDependingComponentMetric
import com.sortinghat.backend.metrics_extractor.services.sync_coupling_dimension.OperationsInvokedMetric

class ExtractSystemMetrics(private val services: Set<Service>) {

    fun execute(): Extractions {
        val sizeMetricsExtractors = listOf(
            SystemComponentsMetric(),
            DeploymentDependencyMetric(),
            OperationsPerComponentMetric(),
            LargestServiceMetric(),
            SmallestServiceMetric()
        )
        val dataCouplingMetricsExtractors = listOf(
            SharedDatabasesMetric(),
            DataSourcesPerComponentMetric(),
            DatabasesByAccessTypeMetric(DatabaseAccessType.Read),
            DatabasesByAccessTypeMetric(DatabaseAccessType.Write),
            DatabasesByAccessTypeMetric(DatabaseAccessType.ReadWrite)
        )
        val syncCouplingMetricsExtractors = listOf(
            ClientsThatInvokeOperationsMetric(),
            ComponentsThatHaveOperationsInvokedMetric(),
            OperationsInvokedMetric(),
            OperationsInvokedByEachDependingComponentMetric()
        )
        val asyncCouplingMetricsExtractors = listOf(
            ClientsThatConsumeMessagesPublishedMetric(),
            ComponentsThatHaveMessagesConsumedMetric(),
            ComponentsThatConsumeQueueMessagesMetric(),
            ComponentsThatPublishQueueMessagesMetric(),
            MessagesConsumedMetric(),
            MessagesConsumedByEachDependingComponentMetric()
        )

        sizeMetricsExtractors.forEach { extractor ->
            services.forEach { service -> service.accept(extractor) }
        }
        dataCouplingMetricsExtractors.forEach { extractor ->
            services.forEach { service -> service.accept(extractor) }
        }
        syncCouplingMetricsExtractors.forEach { extractor ->
            services.forEach { service -> service.accept(extractor) }
        }
        asyncCouplingMetricsExtractors.forEach { extractor ->
            services.forEach { service -> service.accept(extractor) }
        }

        return Extractions(
            size = sizeMetricsExtractors.fold(mapOf()) { acc, extractor ->
                acc.plus(extractor.getMetricDescription() to extractor.getResult())
            },
            dataCoupling = dataCouplingMetricsExtractors.fold(mapOf()) { acc, extractor ->
                acc.plus(extractor.getMetricDescription() to extractor.getResult())
            },
            syncCoupling = syncCouplingMetricsExtractors.fold(mapOf()) { acc, extractor ->
                acc.plus(extractor.getMetricDescription() to extractor.getResult())
            },
            asyncCoupling = asyncCouplingMetricsExtractors.fold(mapOf()) { acc, extractor ->
                acc.plus(extractor.getMetricDescription() to extractor.getResult())
            },
        )
    }
}
