package com.yariv.ofek.taxicounter.model.repository

import com.yariv.ofek.taxicounter.model.network.WazeAPI
import com.yariv.ofek.taxicounter.use_case.LanguageUseCase
import com.yariv.ofek.taxicounter.use_case.LocationUseCase
import timber.log.Timber
import javax.inject.Inject

const val EXP = "8,10,12"//not sure what this is, but its in the waze url

class WazeRepository(
    private val wazeAPI: WazeAPI,
    private val languageUseCase: LanguageUseCase,
    private val locationUseCase: LocationUseCase
) {
    suspend fun getAutoCompleteSuggestions(query: String): List<String> {
        val results = mutableListOf<String>()
        val location = locationUseCase.getLocation()
        try {
            val response = wazeAPI.getAutoCompleteSuggestions(
                query = query,
                exp = EXP,
                sll = "${location.latitude},${location.longitude}",
                lang = languageUseCase.getLanguage()
            )
            for (i in 0 until minOf(response.size, 3)) {//todo const
                if (response[i].name != null) {
                    val formattedAddress =
                        response[i].address?.replace(", (ישראל|Israel)".toRegex(), "")
                    results.add("${response[i].name}, $formattedAddress")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.e(e)
        }
        return results
    }
}