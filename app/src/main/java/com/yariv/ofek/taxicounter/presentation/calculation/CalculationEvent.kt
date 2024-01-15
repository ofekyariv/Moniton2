package com.yariv.ofek.taxicounter.presentation.calculation

import java.time.LocalDateTime

sealed class CalculationEvent {
    data class OnOriginQueryChanged(val query: String) : CalculationEvent()
    data class OnDestinationQueryChanged(val query: String) : CalculationEvent()
    data class OnSelectedDateTimeChanged(val selectedDateTime: LocalDateTime) : CalculationEvent()
    data object OnCalculateClicked : CalculationEvent()
}