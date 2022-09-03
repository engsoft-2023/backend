package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.DockerCompose
import com.sortinghat.backend.data_collector.exceptions.UnableToParseDataException
import com.sortinghat.backend.data_collector.utils.FetchResponse
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

class ParseDockerComposeTest {

    @Nested
    @DisplayName("when parsing is successful")
    inner class SuccessfulParsing {
        private lateinit var dockerCompose: DockerCompose

        @BeforeEach
        fun init() {
            val dockerComposeString = """
            version: "3.7"
            services:
                db:
                    image: mongo:4.2-bionic
                    restart: always
                    container_name: my_db
                    environment:
                        DB_USER: user
                        DB_PASSWORD: password
                    networks:
                        my_network:
                            drive: bridge
                app:
                    build: .docker/
                    command: npm run dev
                    entrypoint: ./entrypoint.sh
                    env_file:
                        - .env
                    volumes:
                        - .:/usr/src/app
                    ports:
                        - 27017:27017
                    links:
                        - "db:database"
                    depends_on:
                        - db
            networks:
                my_network:
                    driver: bridge
        """.trimIndent()
            val parser = ParseDockerCompose()
            dockerCompose = parser.execute(FetchResponse("my-system", dockerComposeString)) as DockerCompose
        }

        @Test
        fun `should return a docker compose with two containers`() {
            assertEquals(2, dockerCompose.services!!.size)
            assertEquals("[app, db]", dockerCompose.services!!.keys.sorted().toString())
        }

        @Test
        fun `should return a docker compose with one network`() {
            assertEquals(1, dockerCompose.networks!!.size)
            assertEquals("[my_network]", dockerCompose.networks!!.keys.toString())
            assertEquals("bridge", dockerCompose.networks!!["my_network"]!!.driver)
        }

        @Test
        fun `should return containers with valid attributes`() {
            val db = dockerCompose.services!!["db"]!!
            assertEquals("mongo:4.2-bionic", db.image)
            assertEquals("always", db.restart)
            assertEquals("my_db", db.container_name)
            assertNotNull(db.environment)
            assertNotNull(db.networks)

            val app = dockerCompose.services!!["app"]!!
            assertEquals(".docker/", app.build)
            assertEquals("npm run dev", app.command)
            assertEquals("./entrypoint.sh", app.entrypoint)
            assertEquals(".env", app.env_file!![0])
            assertEquals(".:/usr/src/app", app.volumes!![0])
            assertEquals("27017:27017", app.ports!![0])
            assertEquals("db:database", app.links!![0])
            assertEquals("db", app.depends_on!![0])
        }
    }

    @Nested
    @DisplayName("when parsing fails")
    inner class ParsingFails {

        @Test
        fun `should throw an exception`() {
            val dockerComposeString = """
                foo: bar
            """.trimIndent()
            val parser = ParseDockerCompose()
            assertThrows<UnableToParseDataException> {
                parser.execute(FetchResponse("my-system", dockerComposeString)) as DockerCompose
            }
        }
    }
}