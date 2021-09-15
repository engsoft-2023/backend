package core.entities

data class CtxDbEdge(
    val ctx: Context,
    val db: Database
) : Edge(ctx, db)
