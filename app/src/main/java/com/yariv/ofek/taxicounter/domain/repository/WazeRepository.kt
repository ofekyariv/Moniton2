package com.yariv.ofek.taxicounter.domain.repository

interface WazeRepository {
    suspend fun getAutoCompleteSuggestions(query: String): List<String>
}