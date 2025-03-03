package com.api.tvmaze.core.realm

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

lateinit var appContext: Context

object RealmManager {
    fun initRealm() {
        val context = appContext
        Realm.init(context)

        val config = RealmConfiguration.Builder()
            .name("realm")
            .schemaVersion(1)
            .allowWritesOnUiThread(false)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(config)
    }
}