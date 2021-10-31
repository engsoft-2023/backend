package com.sortinghat.static_collector.domain.entities.platform_independent_model

import com.sortinghat.static_collector.domain.entities.base.Edge

data class CtxDbEdge(
    val ctx: Context,
    val db: Database
) : Edge(ctx, db)
