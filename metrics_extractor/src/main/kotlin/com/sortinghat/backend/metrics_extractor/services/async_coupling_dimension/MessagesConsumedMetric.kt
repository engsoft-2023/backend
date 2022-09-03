package com.sortinghat.backend.metrics_extractor.services.async_coupling_dimension

import com.sortinghat.backend.domain.behaviors.Visitor
import com.sortinghat.backend.domain.behaviors.VisitorBag
import com.sortinghat.backend.domain.model.MessageChannel
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.metrics_extractor.services.MetricExtractor
import com.sortinghat.backend.metrics_extractor.vo.PerComponentResult

class MessagesConsumedMetric(
    private val visitorBag: VisitorBag = VisitorBag()
) : MetricExtractor, Visitor by visitorBag {

    private val serviceToPublishedChannels = hashMapOf<Service, Set<MessageChannel>>()
    private val channelToSubscribers = hashMapOf<MessageChannel, Set<Service>>()

    override fun getResult(): PerComponentResult {
        val servicesResult = serviceToPublishedChannels.mapValues { (_, channels) ->
            channels
                .filter { channel -> channelToSubscribers.getOrDefault(channel, setOf()).isNotEmpty() }
                .size
        }
        val modulesResult = serviceToPublishedChannels
            .keys
            .groupBy { service -> service.module }
            .mapValues { (_, services) ->
                services.fold(setOf<MessageChannel>()) { acc, service ->
                    acc.plus(
                        serviceToPublishedChannels.getOrDefault(service, setOf())
                    )
                }
            }
            .mapValues { (module, channels) ->
                channels
                    .filter { channel ->
                        channelToSubscribers
                            .getOrDefault(channel, setOf()).any { service -> service.module != module }
                    }
                    .size
            }

        return PerComponentResult(
            modules = modulesResult.mapKeys { it.key.name },
            services = servicesResult.mapKeys { it.key.name },
        )
    }

    override fun getMetricDescription(): String {
        return "Number of different types of messages consumed from other components"
    }

    override fun visit(s: Service) {
        if (s in visitorBag.visited) return

        visitorBag.addVisited(s)
        serviceToPublishedChannels[s] = s.channelsPublishing
        s.channelsSubscribing.forEach { channel ->
            channelToSubscribers.merge(channel, setOf(s)) { old, new -> old.plus(new) }
        }
        s.children().forEach { it.accept(this) }
    }
}
