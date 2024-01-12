package com.yariv.ofek.taxicounter.model.network

import retrofit2.http.GET
import retrofit2.http.Query

interface WazeAPI {
    @GET("live-map/api/autocomplete/")
    suspend fun getAutoCompleteSuggestions(
        @Query("q") query: String,
        @Query("exp") exp: String,
        @Query("sll") sll: String,
        @Query("lang") lang: String
    ): List<AutoCompleteResponse>
}

data class AutoCompleteResponse(
    val name: String? = null,
    val cleanName: String? = null,
    val address: String? = null,
    val venueId: String? = null,
    val location: Location? = null
)

data class Location(
    val latitude: Double,
    val longitude: Double
)