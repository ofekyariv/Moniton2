package com.yariv.ofek.taxicounter.model.network

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class WazeAPITest {

    private val mockWebServer = MockWebServer()
    private lateinit var wazeAPI: WazeAPI

    @Before
    fun setup() {
        mockWebServer.start()
        wazeAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WazeAPI::class.java)
    }

    @Test
    fun `getAutoCompleteSuggestions returns data successfully`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("[{\"name\":\"Place 1\",\"address\":\"Address 1\"},{\"name\":\"Place 2\",\"address\":\"Address 2\"}]")
        mockWebServer.enqueue(mockResponse)

        val response = wazeAPI.getAutoCompleteSuggestions("query", "exp", "sll", "lang")

        assertNotNull(response)
        assertEquals(2, response.size)
        assertEquals("Place 1", response[0].name)
        assertEquals("Address 1", response[0].address)
        assertEquals("Place 2", response[1].name)
        assertEquals("Address 2", response[1].address)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
