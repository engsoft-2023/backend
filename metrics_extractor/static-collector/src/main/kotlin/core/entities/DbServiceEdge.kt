package core.entities

data class DbServiceEdge(
    val db: Database,
    val service: Service,
    val payload: Any? = null
) : Edge(db, service, payload)
