package domain.entities.platform_independent_model

data class CtxServiceEdge(
    val ctx: Context,
    val service: Service
) : Edge(ctx, service)
