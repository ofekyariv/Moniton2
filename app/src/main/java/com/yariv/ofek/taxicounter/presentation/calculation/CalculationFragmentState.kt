package com.yariv.ofek.taxicounter.presentation.calculation

import com.yariv.ofek.taxicounter.other.util.UiText
import java.time.LocalDateTime

data class CalculationFragmentState(
    val origin: String = "",
    val destination: String = "",
    val originSuggestions: List<String> = emptyList(),
    val destinationSuggestions: List<String> = emptyList(),
    val selectedDateTime: LocalDateTime = LocalDateTime.now(),
    val calculationsLeftForToday: Int = 3,
    val isLoading: Boolean = false,
    val error: UiText? = null
)