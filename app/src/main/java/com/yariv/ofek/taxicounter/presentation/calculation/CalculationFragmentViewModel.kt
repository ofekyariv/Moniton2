package com.yariv.ofek.taxicounter.presentation.calculation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yariv.ofek.taxicounter.domain.repository.WazeRepository
import com.yariv.ofek.taxicounter.other.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalculationFragmentViewModel @Inject constructor(
    private val repository: WazeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CalculationFragmentState())
    val state = _state.asStateFlow()


    private var autoCompleteJob: Job? = null

    fun onEvent(event: CalculationEvent) {
        when (event) {
            is CalculationEvent.OnOriginQueryChanged -> onOriginQueryChanged(event.query)
            is CalculationEvent.OnDestinationQueryChanged -> onDestinationQueryChanged(event.query)
            is CalculationEvent.OnSelectedDateTimeChanged -> onDateTimeSelected(event.selectedDateTime)
            is CalculationEvent.OnCalculateClicked -> onCalculationButtonClick()
        }
    }

    private fun onOriginQueryChanged(query: String) {
        _state.update { it.copy(origin = query) }
        debounceAutoCompleteSuggestions(query, QueryType.ORIGIN)
    }

    private fun onDestinationQueryChanged(query: String) {
        _state.update { it.copy(destination = query) }
        debounceAutoCompleteSuggestions(query, QueryType.DESTINATION)
    }

    private fun onDateTimeSelected(dateTime: LocalDateTime) {
        _state.update { it.copy(selectedDateTime = dateTime) }
    }

    private fun onCalculationButtonClick() {

    }

    private fun debounceAutoCompleteSuggestions(query: String, queryType: QueryType) {
        autoCompleteJob?.cancel()
        autoCompleteJob = viewModelScope.launch {
            delay(Constants.AUTOCOMPLETE_DELAY)
            getAutoCompleteSuggestions(query, queryType)
        }
    }

    private fun getAutoCompleteSuggestions(query: String, queryType: QueryType) {
        viewModelScope.launch {
            val results = repository.getAutoCompleteSuggestions(query)
            when (queryType) {
                QueryType.ORIGIN -> _state.update { it.copy(originSuggestions = results) }
                QueryType.DESTINATION -> _state.update { it.copy(destinationSuggestions = results) }
            }
        }
    }
}

enum class QueryType {
    ORIGIN,
    DESTINATION
}