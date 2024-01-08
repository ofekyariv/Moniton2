package com.yariv.ofek.taxicounter

import android.app.Application
import com.yariv.ofek.taxicounter.other.initializer.Initializer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MonitonApplication : Application() {
    @Inject
    lateinit var initializers: Set<@JvmSuppressWildcards Initializer>
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        initializers.forEach { it.initialize() }
    }
}