package domain.entities.platform_independent_model

import domain.entities.base.Edge

data class DbServiceEdge(
    val db: Database,
    val service: Service,
    val payload: Any? = null
) : Edge(db, service, payload)
