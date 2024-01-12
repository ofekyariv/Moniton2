package com.yariv.ofek.taxicounter.calculation

sealed class CalculationEvent {
    data class OnOriginQueryChanged(val query: String) : CalculationEvent()
    data class OnDestinationQueryChanged(val query: String) : CalculationEvent()
    data class OnSelectedDateTimeChanged(val selectedDateTime: String) : CalculationEvent()
    data object OnCalculateClicked : CalculationEvent()
}