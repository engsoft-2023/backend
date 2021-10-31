package com.sortinghat.static_collector.domain.entities.platform_independent_model

import com.sortinghat.static_collector.domain.entities.base.Edge

data class DbServiceEdge(
    val db: Database,
    val service: Service,
    val payload: Any? = null
) : Edge(db, service, payload)
