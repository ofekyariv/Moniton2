package com.yariv.ofek.taxicounter.di

import com.yariv.ofek.taxicounter.use_case.LanguageUseCase
import com.yariv.ofek.taxicounter.use_case.LocationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideLanguageUseCase(): LanguageUseCase = LanguageUseCase()

    @Provides
    fun provideLocationUseCase(): LocationUseCase = LocationUseCase()
}