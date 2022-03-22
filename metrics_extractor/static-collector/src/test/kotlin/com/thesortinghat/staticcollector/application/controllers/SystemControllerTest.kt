package com.thesortinghat.staticcollector.application.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.thesortinghat.staticcollector.application.dto.RegisterSystemDto
import com.thesortinghat.staticcollector.application.dto.SystemDto
import com.thesortinghat.staticcollector.domain.entities.ServiceBasedSystem
import com.thesortinghat.staticcollector.domain.exceptions.EntityAlreadyExistsException
import com.thesortinghat.staticcollector.domain.exceptions.EntityNotFoundException
import com.thesortinghat.staticcollector.domain.exceptions.UnableToFetchDataException
import com.thesortinghat.staticcollector.domain.exceptions.UnableToParseDataException
import com.thesortinghat.staticcollector.domain.services.ExtractDataService
import com.thesortinghat.staticcollector.domain.services.GetSystemService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [SystemController::class])
@DisplayName("SystemController")
class SystemControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var extractDataService: ExtractDataService

    @MockBean
    private lateinit var getSystemService: GetSystemService

    @Nested
    @DisplayName("POST /systems")
    inner class CreateSystemRequest {

        private val request = RegisterSystemDto("https://foo.com/bar", "prod-compose.yml")

        @Test
        fun `should create a system successfully`() {
            val system = ServiceBasedSystem("my-system")

            `when`(extractDataService.run(request.repoUrl, request.filename))
                    .thenReturn(system)

            val response = mockMvc.perform(post("/systems")
                    .contentType("application/json")
                    .content(ObjectMapper().writeValueAsString(request))
            )

            val body = SystemDto(system)
            response.andExpect(status().isCreated)
            response.andExpect(content().json(ObjectMapper().writeValueAsString(body)))
        }

        @Test
        fun `should return a not found error when collector cannot fetch the data`() {
            `when`(extractDataService.run(request.repoUrl, request.filename))
                    .thenThrow(UnableToFetchDataException::class.java)

            mockMvc.perform(post("/systems")
                    .contentType("application/json")
                    .content(ObjectMapper().writeValueAsString(request))
            ).andExpect(status().isNotFound)
        }

        @Test
        fun `should return a bad request error when collector cannot parse the data`() {
            `when`(extractDataService.run(request.repoUrl, request.filename))
                    .thenThrow(UnableToParseDataException::class.java)

            mockMvc.perform(post("/systems")
                    .contentType("application/json")
                    .content(ObjectMapper().writeValueAsString(request))
            ).andExpect(status().isBadRequest)
        }

        @Test
        fun `should return a conflict error when system already exists`() {
            `when`(extractDataService.run(request.repoUrl, request.filename))
                    .thenThrow(EntityAlreadyExistsException::class.java)

            mockMvc.perform(post("/systems")
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
            val system = ServiceBasedSystem("my-system")

            `when`(getSystemService.run(system.id)).thenReturn(system)

            mockMvc.perform(get("/systems/{id}", system.id))
                    .andExpect(status().isOk)
                    .andExpect(content().json(ObjectMapper().writeValueAsString(system)))
        }

        @Test
        fun `should return a not found error when collector does not find the system`() {
            `when`(getSystemService.run("1")).thenThrow(EntityNotFoundException::class.java)
            mockMvc.perform(get("/systems/{id}", "1")).andExpect(status().isNotFound)
        }
    }
}
