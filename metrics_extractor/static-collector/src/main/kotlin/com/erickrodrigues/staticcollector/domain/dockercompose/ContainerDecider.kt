package com.erickrodrigues.staticcollector.domain.dockercompose

class ContainerDecider {
    init {
        throw Exception("class cannot be instantiated")
    }

    companion object {
        private val databaseImages = listOf(
            "mongo" to ("MongoDB" to "NoSQL"),
            "postgres" to ("PostgreSQL" to "Relational"),
            "mysql" to ("MySQL" to "Relational"),
            "mariadb" to ("MariaDB" to "Relational"),
            "neo4j" to ("Neo4J" to "Graph-Oriented")
        )

        fun isDatabase(container: DockerContainer) =
            !container.image.isNullOrEmpty() && databaseImages.any {
                 container.image!!.contains(it.first, true)
            }

        fun isService(container: DockerContainer) =
            !isDatabase(container) && !container.build.isNullOrEmpty()

        fun getDatabaseMakeAndModel(image: String) =
            databaseImages.find { image.contains(it.first, true) }?.second
    }
}