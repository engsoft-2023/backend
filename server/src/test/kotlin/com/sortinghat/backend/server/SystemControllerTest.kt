package com.sortinghat.backend.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.sortinghat.backend.data_collector.exceptions.EntityAlreadyExistsException
import com.sortinghat.backend.data_collector.exceptions.UnableToFetchDataException
import com.sortinghat.backend.data_collector.exceptions.UnableToParseDataException
import com.sortinghat.backend.data_collector.services.RegisterNewSystem
import com.sortinghat.backend.domain.exceptions.EntityNotFoundException
import com.sortinghat.backend.domain.model.Module
import com.sortinghat.backend.domain.model.Service
import com.sortinghat.backend.domain.model.ServiceBasedSystem
import com.sortinghat.backend.metrics_extractor.services.ExtractSystemMetrics
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootTest(classes = [SystemController::class])
@AutoConfigureMockMvc
@EnableWebMvc
@DisplayName("SystemController")
class SystemControllerTest {

    @MockBean
    private lateinit var registerNewSystem: RegisterNewSystem

    @MockBean
    private lateinit var extractSystemMetrics: ExtractSystemMetrics

    @MockBean
    private lateinit var systemService: SystemService

    private val mockMvc by lazy {
        MockMvcBuilders
            .standaloneSetup(
                SystemController(
                    registerNewSystem, extractSystemMetrics, systemService
                )
            )
            .setControllerAdvice(SystemControllerAdvice())
            .build()
    }

    @Nested
    @DisplayName("POST /systems")
    inner class CreateSystemRequest {

        private val request = RegisterSystemDto("https://foo.com/bar", "prod-compose.yml")

        @Test
        fun `should create a system successfully`() {
            val services = setOf(Service(
                name = "service-a",
                responsibility = "",
                module = Module("module"),
                system = ServiceBasedSystem("my-system", "")
            ))
            val system = SystemDto.createFromServices(services)

            Mockito.`when`(registerNewSystem.execute(request.repoUrl, request.filename))
                .thenReturn(services)

            val response = mockMvc.perform(
                MockMvcRequestBuilders.post("/systems")
                    .contentType("application/json")
                    .content(ObjectMapper().writeValueAsString(request))
            )

            response.andExpect(MockMvcResultMatchers.status().isCreated)
            response.andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(system)))
        }

        @Test
        fun `should return a not found error when collector cannot fetch the data`() {
            Mockito.`when`(registerNewSystem.execute(request.repoUrl, request.filename))
                .thenThrow(UnableToFetchDataException::class.java)

            mockMvc.perform(
                MockMvcRequestBuilders.post("/systems")
                    .contentType("application/json")
                    .content(ObjectMapper().writeValueAsString(request))
            ).andExpect(MockMvcResultMatchers.status().isNotFound)
        }

        @Test
        fun `should return a bad request error when collector cannot parse the data`() {
            Mockito.`when`(registerNewSystem.execute(request.repoUrl, request.filename))
                .thenThrow(UnableToParseDataException::class.java)

            mockMvc.perform(
                MockMvcRequestBuilders.post("/systems")
                    .contentType("application/json")
                    .content(ObjectMapper().writeValueAsString(request))
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        fun `should return a conflict error when system already exists`() {
            Mockito.`when`(registerNewSystem.execute(request.repoUrl, request.filename))
                .thenThrow(EntityAlreadyExistsException::class.java)

            mockMvc.perform(
                MockMvcRequestBuilders.post("/systems")
                    .contentType("application/json")
                    .content(ObjectMapper().writeValueAsString(request))
            ).andExpect(MockMvcResultMatchers.status().isConflict)
        }
    }

    @Nested
    @DisplayName("GET /systems/{id}")
    inner class GetSystemRequest {

        @Test
        fun `should return the correct system`() {
            val services = setOf(Service(
                name = "service-a",
                responsibility = "",
                module = Module("module"),
                system = ServiceBasedSystem("my-system", "")
            ))
            val system = SystemDto.createFromServices(services)

            Mockito.`when`(systemService.findAllServicesBySystem(system.id)).thenReturn(services)

            mockMvc.perform(MockMvcRequestBuilders.get("/systems/{id}", system.id))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(system)))
        }

        @Test
        fun `should return a not found error when collector does not find the system`() {
            Mockito.`when`(systemService.findAllServicesBySystem("1")).thenThrow(EntityNotFoundException::class.java)
            mockMvc.perform(
                MockMvcRequestBuilders.get(
                    "/systems/{id}",
                    "1"
                )
            ).andExpect(MockMvcResultMatchers.status().isNotFound)
        }
    }
}
