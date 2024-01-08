package com.yariv.ofek.taxicounter.presentation.navigation

import com.yariv.ofek.taxicounter.R
import com.yariv.ofek.taxicounter.other.util.UiText

enum class NavigationItem(
    val route: String,
    val icon: Int,
    val title: UiText
) {
    RealTimeCounter(
        route = "realTimeCounter",
        icon = R.drawable.ic_counter,
        title = UiText.StringResource(R.string.open_counter)
    ),
    Calculation(
        route = "calculation",
        icon = R.drawable.ic_calculator,
        title = UiText.StringResource(R.string.calculate_ride)
    ),
    Order(
        route = "order",
        icon = R.drawable.ic_taxi,
        title = UiText.StringResource(R.string.to_order_taxi)
    )
}