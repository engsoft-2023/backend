package domain.entities.platform_independent_model

import domain.entities.base.Vertex

data class Database(val name: String, val make: String, val model: String): Vertex()