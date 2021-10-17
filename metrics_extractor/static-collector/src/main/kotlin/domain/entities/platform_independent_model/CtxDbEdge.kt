package domain.entities.platform_independent_model

data class CtxDbEdge(
    val ctx: Context,
    val db: Database
) : Edge(ctx, db)
