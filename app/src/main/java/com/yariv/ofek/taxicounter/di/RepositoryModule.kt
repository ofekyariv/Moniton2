package com.yariv.ofek.taxicounter.di

import com.yariv.ofek.taxicounter.domain.repository.WazeRepository
import com.yariv.ofek.taxicounter.domain.use_case.LanguageUseCase
import com.yariv.ofek.taxicounter.domain.use_case.LocationUseCase
import com.yariv.ofek.taxicounter.model.network.WazeAPI
import com.yariv.ofek.taxicounter.model.repository.WazeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideWazeRepository(
        wazeAPI: WazeAPI,
        languageUseCase: LanguageUseCase,
        locationUseCase: LocationUseCase
    ): WazeRepository =
        WazeRepositoryImpl(
            wazeAPI = wazeAPI,
            languageUseCase = languageUseCase,
            locationUseCase = locationUseCase
        )
}