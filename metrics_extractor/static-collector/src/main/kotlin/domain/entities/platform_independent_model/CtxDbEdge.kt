package domain.entities.platform_independent_model

import domain.entities.base.Edge

data class CtxDbEdge(
    val ctx: Context,
    val db: Database
) : Edge(ctx, db)
