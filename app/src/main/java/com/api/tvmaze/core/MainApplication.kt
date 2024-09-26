package com.api.tvmaze.core

import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        RealmManager.initRealm()
    }
}
