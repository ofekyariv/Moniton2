package com.yariv.ofek.taxicounter.model.repository

import com.yariv.ofek.taxicounter.domain.repository.WazeRepository
import com.yariv.ofek.taxicounter.domain.use_case.LanguageUseCase
import com.yariv.ofek.taxicounter.domain.use_case.LocationUseCase
import com.yariv.ofek.taxicounter.model.network.AutoCompleteResponse
import com.yariv.ofek.taxicounter.model.network.Location
import com.yariv.ofek.taxicounter.model.network.WazeAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class WazeRepositoryTest {

    @Mock
    private lateinit var wazeRepository: WazeRepository

    @Mock
    private lateinit var locationUseCase: LocationUseCase

    @Mock
    private lateinit var languageUseCase: LanguageUseCase

    @Mock
    private lateinit var wazeAPI: WazeAPI

    private val testDispatcher = StandardTestDispatcher()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        wazeRepository = WazeRepositoryImpl(wazeAPI, languageUseCase, locationUseCase)

    }

    @Test
    fun `getAutoCompleteSuggestions returns empty list when no internet connection`() =
        runBlocking {
            `when`(
                wazeAPI.getAutoCompleteSuggestions(
                    anyString(),
                    anyString(),
                    anyString(),
                    anyString()
                )
            )
                .thenThrow(RuntimeException("No internet connection"))
            val suggestions = wazeRepository.getAutoCompleteSuggestions("query")
            assertTrue(suggestions.isEmpty())
        }

    @Test
    fun `getAutoCompleteSuggestions returns suggestions successfully`() = runBlocking {
        `when`(locationUseCase.getLocation()).thenReturn(Location(0.0, 0.0))
        `when`(languageUseCase.getLanguage()).thenReturn("en")
        `when`(
            wazeAPI.getAutoCompleteSuggestions(
                query = anyString(),
                exp = anyString(),
                sll = anyString(),
                lang = anyString()
            )
        ).thenReturn(
            listOf(
                AutoCompleteResponse("name"),
                AutoCompleteResponse("name2"),
                AutoCompleteResponse("name3"),
                AutoCompleteResponse("name4"),
                AutoCompleteResponse("name5"),
                AutoCompleteResponse("name6"),
                AutoCompleteResponse("name7")
            )
        )
        val suggestions = wazeRepository.getAutoCompleteSuggestions("query")
        verify(wazeAPI).getAutoCompleteSuggestions(
            query = "query",
            exp = "8,10,12",
            sll = "0.0,0.0",
            lang = "en"
        )
        assertEquals(5, suggestions.size)
        assertEquals("name", suggestions[0])
        assertEquals("name2", suggestions[1])
        assertEquals("name3", suggestions[2])
        assertEquals("name4", suggestions[3])
        assertEquals("name5", suggestions[4])
    }

    @Test
    fun `getAutoCompleteSuggestions handles exceptions`() = runBlocking {
        `when`(locationUseCase.getLocation()).thenReturn(Location(0.0, 0.0))
        `when`(languageUseCase.getLanguage()).thenReturn("en")
        `when`(
            wazeAPI.getAutoCompleteSuggestions(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
        )
            .thenThrow(RuntimeException())
        val suggestions = wazeRepository.getAutoCompleteSuggestions("query")
        verify(wazeAPI).getAutoCompleteSuggestions("query", "8,10,12", "0.0,0.0", "en")
        assertEquals(0, suggestions.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher
    }
}