package domain.entities.platform_independent_model

import domain.entities.base.Edge

data class CtxServiceEdge(
    val ctx: Context,
    val service: Service
) : Edge(ctx, service)
