package com.api.tvmaze.data.datasource.local

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

lateinit var appContext: Context

object RealmManager {

    private var realm: Realm? = null

    fun initRealm() {
        val context = appContext
        Realm.init(context)

        val config = RealmConfiguration.Builder()
            .name("shows.realm")
            .schemaVersion(1)
            .allowWritesOnUiThread(false)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()
    }

    fun getRealmInstance(): Realm {
        if (realm == null) {
            initRealm()
        }
        return realm!!
    }

    fun closeRealm() {
        realm?.close()
        realm = null
    }
}