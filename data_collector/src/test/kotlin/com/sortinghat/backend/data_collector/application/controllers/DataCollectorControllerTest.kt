//package com.sortinghat.backend.data_collector.application.controllers
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.sortinghat.backend.data_collector.application.dto.RegisterSystemDto
//import com.sortinghat.backend.data_collector.application.dto.SystemDto
//import com.sortinghat.backend.data_collector.application.exceptions.EntityAlreadyExistsException
//import com.sortinghat.backend.data_collector.application.exceptions.EntityNotFoundException
//import com.sortinghat.backend.data_collector.application.services.GetSystem
//import com.sortinghat.backend.data_collector.application.services.RegisterNewSystem
//import com.sortinghat.backend.data_collector.domain.exceptions.UnableToFetchDataException
//import com.sortinghat.backend.data_collector.domain.exceptions.UnableToParseDataException
//import com.sortinghat.backend.domain.model.ServiceBasedSystem
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Nested
//import org.junit.jupiter.api.Test
//import org.mockito.Mockito.`when`
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.mock.mockito.MockBean
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//
//@SpringBootTest(classes = [DataCollectorController::class])
//@DisplayName("DataCollectorController")
//class DataCollectorControllerTest {
//
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//    @MockBean
//    private lateinit var registerNewSystem: RegisterNewSystem
//
//    @MockBean
//    private lateinit var getSystem: GetSystem
//
//    @Nested
//    @DisplayName("POST /systems")
//    inner class CreateSystemRequest {
//
//        private val request = RegisterSystemDto("https://foo.com/bar", "prod-compose.yml")
//
//        @Test
//        fun `should create a system successfully`() {
//            val system = SystemDto.createFromSystems(
//                setOf(ServiceBasedSystem("my-system", ""))
//            ).first()
//
//            `when`(registerNewSystem.execute(request.repoUrl, request.filename))
//                .thenReturn(system)
//
//            val response = mockMvc.perform(post("/systems")
//                .contentType("application/json")
//                .content(ObjectMapper().writeValueAsString(request))
//            )
//
//            response.andExpect(status().isCreated)
//            response.andExpect(content().json(ObjectMapper().writeValueAsString(system)))
//        }
//
//        @Test
//        fun `should return a not found error when collector cannot fetch the data`() {
//            `when`(registerNewSystem.execute(request.repoUrl, request.filename))
//                .thenThrow(UnableToFetchDataException::class.java)
//
//            mockMvc.perform(post("/systems")
//                .contentType("application/json")
//                .content(ObjectMapper().writeValueAsString(request))
//            ).andExpect(status().isNotFound)
//        }
//
//        @Test
//        fun `should return a bad request error when collector cannot parse the data`() {
//            `when`(registerNewSystem.execute(request.repoUrl, request.filename))
//                .thenThrow(UnableToParseDataException::class.java)
//
//            mockMvc.perform(post("/systems")
//                .contentType("application/json")
//                .content(ObjectMapper().writeValueAsString(request))
//            ).andExpect(status().isBadRequest)
//        }
//
//        @Test
//        fun `should return a conflict error when system already exists`() {
//            `when`(registerNewSystem.execute(request.repoUrl, request.filename))
//                .thenThrow(EntityAlreadyExistsException::class.java)
//
//            mockMvc.perform(post("/systems")
//                .contentType("application/json")
//                .content(ObjectMapper().writeValueAsString(request))
//            ).andExpect(status().isConflict)
//        }
//    }
//
//    @Nested
//    @DisplayName("GET /systems/{id}")
//    inner class GetSystemRequest {
//
//        @Test
//        fun `should return the correct system`() {
//            val system = SystemDto.createFromSystems(
//                setOf(ServiceBasedSystem("my-system", ""))
//            ).first()
//
//            `when`(getSystem.execute(system.id)).thenReturn(system)
//
//            mockMvc.perform(get("/systems/{id}", system.id))
//                .andExpect(status().isOk)
//                .andExpect(content().json(ObjectMapper().writeValueAsString(system)))
//        }
//
//        @Test
//        fun `should return a not found error when collector does not find the system`() {
//            `when`(getSystem.execute("1")).thenThrow(EntityNotFoundException::class.java)
//            mockMvc.perform(get("/systems/{id}", "1")).andExpect(status().isNotFound)
//        }
//    }
//}
