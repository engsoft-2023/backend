package com.sortinghat.static_collector.domain.usecases

import com.sortinghat.static_collector.domain.converters.ConverterToPIM
import com.sortinghat.static_collector.domain.entities.platform_independent_model.System
import com.sortinghat.static_collector.domain.entities.platform_specific_model.PlatformSpecificModel
import com.sortinghat.static_collector.domain.fetchers.FetchData
import com.sortinghat.static_collector.domain.fetchers.FetchResponse
import com.sortinghat.static_collector.domain.ports.ParseData
import com.sortinghat.static_collector.domain.ports.repositories.SystemRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class ExtractDataUseCaseTest {

    private lateinit var extractDataUseCase: ExtractDataUseCase
    private val fetchData by lazy { mock(FetchData::class.java) }
    private val parseData by lazy { mock(ParseData::class.java) }
    private val converterToPIM by lazy { mock(ConverterToPIM::class.java) }
    private val systemRepository by lazy { mock(SystemRepository::class.java) }
    private val system = System("Sorting Hat")

    @BeforeEach
    fun init() {
        val response = FetchResponse("", "")
        val psm = mock(PlatformSpecificModel::class.java)
        `when`(fetchData.run(anyString())).thenReturn(response)
        `when`(parseData.run(response)).thenReturn(psm)
        `when`(converterToPIM.run(psm)).thenReturn(system)
        extractDataUseCase = ExtractDataUseCase(fetchData, parseData, converterToPIM, systemRepository)
    }

    @Test
    @DisplayName("call extract use case for a system which name already exists will throw an exception")
    fun testWhenSystemAlreadyExists() {
        `when`(systemRepository.findByName(anyString())).thenReturn(System("Sorting Hat"))
        assertThrows(Exception::class.java) { extractDataUseCase.run("https://github.com") }
    }

    @Test
    @DisplayName("it works properly for a system which name doesn't exist")
    fun testWhenSystemDoesNotExist() {
        `when`(systemRepository.findByName(anyString())).thenReturn(null)
        val s = extractDataUseCase.run("https://github.com")
        verify(systemRepository, times(1)).save(system)
        assertEquals(system, s)
    }
}