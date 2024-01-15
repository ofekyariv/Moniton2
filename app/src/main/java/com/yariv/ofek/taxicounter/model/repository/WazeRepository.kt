package com.yariv.ofek.taxicounter.model.repository

import com.yariv.ofek.taxicounter.domain.repository.WazeRepository
import com.yariv.ofek.taxicounter.domain.use_case.LanguageUseCase
import com.yariv.ofek.taxicounter.domain.use_case.LocationUseCase
import com.yariv.ofek.taxicounter.model.network.WazeAPI
import com.yariv.ofek.taxicounter.other.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

const val EXP = "8,10,12"//not sure what this is, but its in the waze url

class WazeRepositoryImpl(
    private val wazeAPI: WazeAPI,
    private val languageUseCase: LanguageUseCase,
    private val locationUseCase: LocationUseCase
) : WazeRepository {

    private var currentCall: Job? = null
    override suspend fun getAutoCompleteSuggestions(query: String): List<String> {
        currentCall?.cancel()
        val results = mutableListOf<String>()
        val location = locationUseCase.getLocation()
        currentCall = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = wazeAPI.getAutoCompleteSuggestions(
                    query = query,
                    exp = EXP,
                    sll = "${location.latitude},${location.longitude}",
                    lang = languageUseCase.getLanguage()
                )
                val numberOfResults = minOf(response.size, Constants.AUTOCOMPLETE_SUGGESTIONS_COUNT)
                response.take(numberOfResults).forEach {
                    if (it.name != null) {
                        val formattedAddress = it.address?.replace(", (ישראל|Israel)".toRegex(), "")
                        if (formattedAddress != null) {
                            results.add("${it.name}, $formattedAddress")
                        } else {
                            results.add(it.name)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
            }
        }
        currentCall?.join()
        return results.also { currentCall = null }
    }
}