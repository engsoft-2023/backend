package com.sortinghat.static_collector.domain.entities.platform_independent_model

import com.sortinghat.static_collector.domain.entities.base.Vertex

data class Database(val name: String, val make: String, val model: String): Vertex()