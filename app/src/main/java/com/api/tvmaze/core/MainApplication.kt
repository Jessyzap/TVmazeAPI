package com.api.tvmaze.core

import android.app.Application
import com.api.tvmaze.core.realm.RealmManager
import com.api.tvmaze.core.realm.appContext
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        RealmManager.initRealm()
    }
}
