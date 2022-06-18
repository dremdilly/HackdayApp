package com.daurenbek.hackdayapp

import android.app.Application
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        PreferenceManager.initPreferences(applicationContext)

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}