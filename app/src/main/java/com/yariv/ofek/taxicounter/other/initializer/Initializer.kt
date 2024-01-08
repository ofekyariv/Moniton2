package com.yariv.ofek.taxicounter.other.initializer

import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.yariv.ofek.taxicounter.R

interface Initializer {
    fun initialize()
}


class FirebaseInitializer(private val context: Context) : Initializer {
    override fun initialize() {
        FirebaseAuth.getInstance().signInAnonymously()
        FirebaseRemoteConfig.getInstance().apply {
            setDefaultsAsync(R.xml.remote_config_defaults)
            fetchAndActivate()
        }
    }
}

class AdsInitializer(private val context: Context) : Initializer {
    override fun initialize() {
        MobileAds.initialize(context) {}
    }
}
