package core.entities

data class CtxServiceEdge(
    val ctx: Context,
    val service: Service
) : Edge(ctx, service)
