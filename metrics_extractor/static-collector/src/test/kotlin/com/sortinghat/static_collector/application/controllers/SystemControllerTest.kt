package com.sortinghat.static_collector.application.controllers

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
@SpringBootTest
class SystemControllerTest(@Autowired private val mockMvc: MockMvc) {

    @Test
    fun `register system by analyzing docker-compose file`() {
        mockMvc
            .post("/systems") {
                contentType = MediaType.APPLICATION_JSON
                content = "{\"repoUrl\":\"https://github.com/codelab-alexia/buscar-hackathon\"}"
            }.andExpect {
                status { isOk() }
            }
    }
}