package com.api.tvmaze.features.favorite_show.data.datasource.local

import com.api.tvmaze.features.favorite_show.data.model.ShowObject
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FavoriteShowLocalDataSourceImpl : IFavoriteShowLocalDataSource {

    @OptIn(DelicateCoroutinesApi::class)
    private var realmThread = newSingleThreadContext("RealmThread")
    private var coroutineScope = CoroutineScope(realmThread + SupervisorJob())
    private var realm: Realm? = null
    private var isClosed = false

    private suspend fun createOrGetRealmInstance() = withContext(realmThread) {
        if (realm == null || realm?.isClosed == true) {
            try {
                realm = Realm.getDefaultInstance()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun ensureRealm() {
        if (isClosed) {
            realmThread = newSingleThreadContext("RealmThread")
            coroutineScope = CoroutineScope(realmThread + SupervisorJob())
            isClosed = false
        }
        createOrGetRealmInstance()
    }

    override suspend fun getFavoriteShows(): List<ShowObject> = withContext(realmThread) {
        ensureRealm()
        return@withContext try {
            realm?.let { realmInstance ->
                val realmResults: RealmResults<ShowObject> =
                    realmInstance.where(ShowObject::class.java).findAll()
                realmInstance.copyFromRealm(realmResults) ?: emptyList()
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun saveFavoriteShow(show: ShowObject) {
        withContext(realmThread) {
            ensureRealm()
            try {
                realm?.executeTransaction { it.insert(show) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext
        }
    }

    override suspend fun deleteFavoriteShow(show: ShowObject): Boolean = withContext(realmThread) {
        ensureRealm()
        return@withContext try {
            var wasDeleted = false
            realm?.executeTransaction { transaction ->
                val result = transaction.where(ShowObject::class.java)
                    .equalTo("id", show.id)
                    .findFirst()
                if (result != null) {
                    result.deleteFromRealm()
                    wasDeleted = true
                }
            }
            wasDeleted
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun checkIfIsFavorite(showId: Int?): Boolean = withContext(realmThread) {
        ensureRealm()
        return@withContext try {
            val isFavorite = showId?.let {
                realm?.where(ShowObject::class.java)
                    ?.equalTo("id", showId)
                    ?.findFirst()
            } != null
            isFavorite
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun closeRealm() {
        coroutineScope.launch(realmThread) {
            realm?.close()
            realmThread.close()
            coroutineScope.cancel()
            realm = null
            isClosed = true
        }
    }
}
