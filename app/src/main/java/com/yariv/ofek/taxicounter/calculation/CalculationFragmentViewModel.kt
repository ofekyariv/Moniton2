package com.yariv.ofek.taxicounter.calculation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yariv.ofek.taxicounter.model.repository.WazeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalculationFragmentViewModel @Inject constructor(
    private val repository: WazeRepository
) : ViewModel() {

    var state by mutableStateOf(CalculationFragmentState())
        private set

    fun onOriginQueryChanged(query: String) {
        state = state.copy(origin = query)
        getAutoCompleteSuggestions(query, QueryType.ORIGIN)
    }

    fun onDestinationQueryChanged(query: String) {
        state = state.copy(destination = query)
        getAutoCompleteSuggestions(query, QueryType.DESTINATION)
    }

    fun onDateTimeSelected(dateTime: LocalDateTime) {
        state = state.copy(selectedDateTime = dateTime)
    }

    fun onCalculationButtonClick() {

    }

    private fun getAutoCompleteSuggestions(query: String, queryType: QueryType) {
        viewModelScope.launch {
            val results = repository.getAutoCompleteSuggestions(query)
            state = when (queryType) {
                QueryType.ORIGIN -> state.copy(originSuggestions = results)
                QueryType.DESTINATION -> state.copy(destinationSuggestions = results)
            }
        }
    }
}
//create enum with origin and destination
enum class QueryType {
    ORIGIN,
    DESTINATION
}