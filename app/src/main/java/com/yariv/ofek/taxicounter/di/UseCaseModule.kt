package com.yariv.ofek.taxicounter.di

import com.yariv.ofek.taxicounter.domain.use_case.LanguageUseCase
import com.yariv.ofek.taxicounter.domain.use_case.LanguageUseCaseImpl
import com.yariv.ofek.taxicounter.domain.use_case.LocationUseCase
import com.yariv.ofek.taxicounter.domain.use_case.LocationUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideLanguageUseCase(): LanguageUseCase = LanguageUseCaseImpl()

    @Provides
    fun provideLocationUseCase(): LocationUseCase = LocationUseCaseImpl()
}