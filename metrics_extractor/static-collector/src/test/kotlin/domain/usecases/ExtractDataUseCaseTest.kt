package domain.usecases

import domain.converters.ConverterToPIM
import domain.entities.platform_independent_model.System
import domain.entities.platform_specific_model.PlatformSpecificModel
import domain.fetchers.FetchData
import domain.ports.ParseData
import domain.ports.repositories.SystemRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ExtractDataUseCaseTest {

    @Mock private lateinit var fetchData: FetchData
    @Mock private lateinit var parseData: ParseData
    @Mock private lateinit var converterToPIM: ConverterToPIM
    @Mock private lateinit var systemRepository: SystemRepository
    private lateinit var extractDataUseCase: ExtractDataUseCase
    private val system = System("Sorting Hat")

    @BeforeEach
    fun init() {
        val psm = mock(PlatformSpecificModel::class.java)
        `when`(fetchData.run(anyString())).thenReturn("")
        `when`(parseData.run(anyString())).thenReturn(psm)
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