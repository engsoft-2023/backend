package com.erickrodrigues.staticcollector.domain.dockercompose

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ContainerDeciderTest {

    @Test
    fun `class cannot be instantiated`() {
        assertThrows<Exception> { ContainerDecider() }
    }

    @Test
    fun `decides container as database correctly`() {
        val container = DockerContainer(null, "mongo:4.2-bionic")
        assertTrue(ContainerDecider.isDatabase(container))
    }

    @Test
    fun `decides container as service correctly`() {
        val container = DockerContainer(".")
        assertTrue(ContainerDecider.isService(container))
    }

}