package domain.entities.platform_specific_model.docker_compose

class ContainerDecider {
    companion object {
        private val databaseImages: List<String> = listOf(
            "mongodb", "postgres", "mysql", "mariadb", "neo4j"
        )

        fun isDatabase(container: DockerContainer) =
            container.image.isNotEmpty() && databaseImages.any {
                    image -> container.image.contains(image, true)
            }

        fun isService(container: DockerContainer) =
            !isDatabase(container) && container.build.isNotEmpty()
    }
}