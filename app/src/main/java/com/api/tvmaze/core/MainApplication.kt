package com.api.tvmaze.core

import android.app.Application
import com.api.tvmaze.data.datasource.local.RealmManager
import com.api.tvmaze.data.datasource.local.appContext

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        RealmManager.initRealm()
    }
}
