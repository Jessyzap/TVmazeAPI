package com.api.tvmaze.data.datasource.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.api.tvmaze.data.model.Show
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ShowLocalDataSourceImpl : IShowLocalDataSource {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun getFavoriteShows(): List<ShowObject> {
        val realm = Realm.getDefaultInstance()
        return try {
            val realmResults: RealmResults<ShowObject> =
                realm.where(ShowObject::class.java).findAll()
            realm.copyFromRealm(realmResults) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override fun saveFavoriteShow(show: Show) {
        coroutineScope.launch(Dispatchers.IO) {
            val realm = Realm.getDefaultInstance()
            try {
                realm.executeTransaction { realmTransaction ->
                    val showObject = Show.mapperShowObject(show)
                    realmTransaction.insert(showObject)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun deleteFavoriteShow(show: Show): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        coroutineScope.launch(Dispatchers.IO) {
            val realm = Realm.getDefaultInstance()
            try {
                realm.executeTransaction { realmTransaction ->
                    val showObject = Show.mapperShowObject(show)
                    val result = realmTransaction.where(ShowObject::class.java)
                        .equalTo("id", showObject.id)
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

    override fun checkIfIsFavorite(showId: Int?): Boolean {
        val realm = Realm.getDefaultInstance()
        return try {
            val result = showId?.let {
                realm.where(ShowObject::class.java)
                    .equalTo("id", showId)
                    .findFirst()
            }
            result != null
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    //todo close realm
}