package com.sortinghat.backend.data_collector.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.sortinghat.backend.data_collector.dto.RegisterSystemDto
import com.sortinghat.backend.data_collector.dto.SystemDto
import com.sortinghat.backend.data_collector.exceptions.EntityAlreadyExistsException
import com.sortinghat.backend.data_collector.exceptions.EntityNotFoundException
import com.sortinghat.backend.data_collector.exceptions.UnableToFetchDataException
import com.sortinghat.backend.data_collector.exceptions.UnableToParseDataException
import com.sortinghat.backend.data_collector.services.GetSystem
import com.sortinghat.backend.data_collector.services.RegisterNewSystem
import com.sortinghat.backend.domain.model.ServiceBasedSystem
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootTest(classes = [DataCollectorController::class])
@AutoConfigureMockMvc
@EnableWebMvc
@DisplayName("DataCollectorController")
class DataCollectorControllerTest {

    @MockBean
    private lateinit var registerNewSystem: RegisterNewSystem

    @MockBean
    private lateinit var getSystem: GetSystem

    private val mockMvc by lazy {
        MockMvcBuilders
            .standaloneSetup(
                DataCollectorController(
                    registerNewSystem,
                    getSystem
                )
            )
            .setControllerAdvice(DataCollectorControllerExceptionHandler())
            .build()
    }

    @Nested
    @DisplayName("POST /systems")
    inner class CreateSystemRequest {

        private val request = RegisterSystemDto("https://foo.com/bar", "prod-compose.yml")

        @Test
        fun `should create a system successfully`() {
            val system = SystemDto.createFromSystems(
                setOf(ServiceBasedSystem("my-system", ""))
            ).first()

            `when`(registerNewSystem.execute(request.repoUrl, request.filename))
                .thenReturn(system)

            val response = mockMvc.perform(
                post("/collection/systems")
                    .contentType("application/json")
                    .content(ObjectMapper().writeValueAsString(request))
            )

            response.andExpect(status().isCreated)
            response.andExpect(content().json(ObjectMapper().writeValueAsString(system)))
        }

        @Test
        fun `should return a not found error when collector cannot fetch the data`() {
            `when`(registerNewSystem.execute(request.repoUrl, request.filename))
                .thenThrow(UnableToFetchDataException::class.java)

            mockMvc.perform(
                post("/collection/systems")
                    .contentType("application/json")
                    .content(ObjectMapper().writeValueAsString(request))
            ).andExpect(status().isNotFound)
        }

        @Test
        fun `should return a bad request error when collector cannot parse the data`() {
            `when`(registerNewSystem.execute(request.repoUrl, request.filename))
                .thenThrow(UnableToParseDataException::class.java)

            mockMvc.perform(
                post("/collection/systems")
                    .contentType("application/json")
                    .content(ObjectMapper().writeValueAsString(request))
            ).andExpect(status().isBadRequest)
        }

        @Test
        fun `should return a conflict error when system already exists`() {
            `when`(registerNewSystem.execute(request.repoUrl, request.filename))
                .thenThrow(EntityAlreadyExistsException::class.java)

            mockMvc.perform(
                post("/collection/systems")
                    .contentType("application/json")
                    .content(ObjectMapper().writeValueAsString(request))
            ).andExpect(status().isConflict)
        }
    }

    @Nested
    @DisplayName("GET /systems/{id}")
    inner class GetSystemRequest {

        @Test
        fun `should return the correct system`() {
            val system = SystemDto.createFromSystems(
                setOf(ServiceBasedSystem("my-system", ""))
            ).first()

            `when`(getSystem.execute(system.id)).thenReturn(system)

            mockMvc.perform(get("/collection/systems/{id}", system.id))
                .andExpect(status().isOk)
                .andExpect(content().json(ObjectMapper().writeValueAsString(system)))
        }

        @Test
        fun `should return a not found error when collector does not find the system`() {
            `when`(getSystem.execute("1")).thenThrow(EntityNotFoundException::class.java)
            mockMvc.perform(get("/collection/systems/{id}", "1")).andExpect(status().isNotFound)
        }
    }
}
