package com.sortinghat.static_collector.domain.entities.platform_independent_model

import com.sortinghat.static_collector.domain.entities.base.Edge

data class CtxServiceEdge(
    val ctx: Context,
    val service: Service
) : Edge(ctx, service)
