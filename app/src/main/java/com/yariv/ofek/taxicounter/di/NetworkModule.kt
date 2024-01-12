package com.yariv.ofek.taxicounter.di

import com.yariv.ofek.taxicounter.model.network.WazeAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val WAZE_RETROFIT = "WAZE_RETROFIT"
private const val BASE_WAZE_URL = "https://www.waze.com/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_WAZE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideWazeAPI(retrofit: Retrofit): WazeAPI =
        retrofit.create(WazeAPI::class.java)
}
