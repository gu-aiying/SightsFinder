package com.example.sightsfinder

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("2b1c963e-2866-4afb-bdec-978fe7904b01")
        MapKitFactory.initialize(this)
    }
}