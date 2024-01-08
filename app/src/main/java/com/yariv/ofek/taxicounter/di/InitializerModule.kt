package com.yariv.ofek.taxicounter.di

import android.content.Context
import com.yariv.ofek.taxicounter.other.initializer.AdsInitializer
import com.yariv.ofek.taxicounter.other.initializer.FirebaseInitializer
import com.yariv.ofek.taxicounter.other.initializer.Initializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object InitializerModule {

    @Provides
    @IntoSet
    fun provideFirebaseInitializer(@ApplicationContext context: Context): Initializer = FirebaseInitializer(context)

    @Provides
    @IntoSet
    fun provideAdsInitializer(@ApplicationContext context: Context): Initializer = AdsInitializer(context)
}
