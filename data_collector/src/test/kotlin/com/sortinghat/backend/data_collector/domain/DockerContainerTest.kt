package com.sortinghat.backend.data_collector.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DockerContainerTest {

    @Test
    fun `decides container as database correctly`() {
        val container = DockerContainer(null, "mongo:4.2-bionic")
        assertTrue(container.isDatabase())
    }

    @Test
    fun `decides container as service correctly`() {
        val container = DockerContainer(".")
        assertTrue(container.isService())
    }

}