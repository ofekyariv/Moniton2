package com.yariv.ofek.taxicounter.presentation.calculation

import com.yariv.ofek.taxicounter.domain.repository.WazeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class CalculationFragmentViewModelTest {

    private lateinit var viewModel: CalculationFragmentViewModel
    private lateinit var repository: WazeRepository
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(WazeRepository::class.java)
        viewModel = CalculationFragmentViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `autocomplete updates after debounce time`() = runTest {
        `when`(repository.getAutoCompleteSuggestions("New York")).thenReturn(listOf("New York"))
        viewModel.onEvent(CalculationEvent.OnOriginQueryChanged("New York"))
        assert(viewModel.state.originSuggestions.isEmpty())
        testDispatcher.apply { advanceTimeBy(500); runCurrent() }
        verify(repository).getAutoCompleteSuggestions("New York")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `rapid successive inputs trigger single query`() = runTest {
        `when`(repository.getAutoCompleteSuggestions("New York")).thenReturn(listOf("New York"))
        viewModel.onEvent(CalculationEvent.OnOriginQueryChanged("New"))
        viewModel.onEvent(CalculationEvent.OnOriginQueryChanged("New Y"))
        viewModel.onEvent(CalculationEvent.OnOriginQueryChanged("New York"))
        testDispatcher.apply { advanceTimeBy(500); runCurrent() }
        verify(repository, times(1)).getAutoCompleteSuggestions("New York")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher
    }
}
