package domain.entities.platform_specific_model.docker_compose

class ContainerDecider {
    companion object {
        private val databaseImages by lazy {
            listOf(
                "mongodb" to ("MongoDB" to "NoSQL"),
                "postgres" to ("PostgreSQL" to "Relational"),
                "mysql" to ("MySQL" to "Relational"),
                "mariadb" to ("MariaDB" to "Relational"),
                "neo4j" to ("Neo4J" to "Graph-Oriented")
            )
        }

        fun isDatabase(container: DockerContainer) =
            container.image.isNotEmpty() && databaseImages.any {
                    (key, _) -> container.image.contains(key, true)
            }

        fun isService(container: DockerContainer) =
            !isDatabase(container) && container.build.isNotEmpty()

        fun getDatabaseMakeAndModel(image: String): Pair<String, String>? =
            databaseImages.find { (key, _) -> image.contains(key, true) }?.second
    }
}