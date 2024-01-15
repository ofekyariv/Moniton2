package com.yariv.ofek.taxicounter.domain.use_case

import com.yariv.ofek.taxicounter.model.network.Location

interface LocationUseCase {
    fun getLocation(): Location
}

class LocationUseCaseImpl : LocationUseCase {
    override fun getLocation(): Location {
        return Location(0.0, 0.0)
    }
}