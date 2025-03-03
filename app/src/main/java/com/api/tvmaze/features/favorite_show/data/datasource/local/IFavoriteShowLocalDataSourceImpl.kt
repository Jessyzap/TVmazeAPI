package com.api.tvmaze.features.favorite_show.data.datasource.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.api.tvmaze.features.favorite_show.data.model.ShowObject
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class IFavoriteShowLocalDataSourceImpl : IFavoriteShowLocalDataSource {

    @OptIn(DelicateCoroutinesApi::class)
    private val realmThread = newSingleThreadContext("RealmThread")
    private val coroutineScope = CoroutineScope(realmThread + SupervisorJob())

    private var realm: Realm? = null

    init {
        coroutineScope.launch(realmThread) {
            realm = Realm.getDefaultInstance()
        }
    }

    override fun getFavoriteShows(): LiveData<List<ShowObject>> {
        val liveData = MutableLiveData<List<ShowObject>>()
        coroutineScope.launch(realmThread) {
            try {
                realm?.let { realmInstance ->
                    val realmResults: RealmResults<ShowObject> =
                        realmInstance.where(ShowObject::class.java).findAll()
                    liveData.postValue(realmInstance.copyFromRealm(realmResults) ?: emptyList())
                } ?: liveData.postValue(emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(emptyList())
            }
        }
        return liveData
    }

    override fun saveFavoriteShow(show: ShowObject) {
        coroutineScope.launch(realmThread) {
            try {
                realm?.executeTransaction { it.insert(show) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun deleteFavoriteShow(show: ShowObject): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        coroutineScope.launch(realmThread) {
            try {
                realm?.executeTransaction { transaction ->
                    val result = transaction.where(ShowObject::class.java)
                        .equalTo("id", show.id)
                        .findFirst()

                    result?.deleteFromRealm()?.let {
                        liveData.postValue(true)
                    } ?: liveData.postValue(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(false)
            }
        }
        return liveData
    }

    override fun checkIfIsFavorite(showId: Int?): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        coroutineScope.launch(realmThread) {
            try {
                val result = showId?.let {
                    realm?.where(ShowObject::class.java)
                        ?.equalTo("id", showId)
                        ?.findFirst()
                }
                liveData.postValue(result != null)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(false)
            }
        }
        return liveData
    }

    override fun closeRealm() {
        coroutineScope.launch(realmThread){
            realm?.close()
            realmThread.close()
            coroutineScope.cancel()
        }
    }
}