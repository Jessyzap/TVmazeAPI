package com.api.tvmaze.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.api.tvmaze.api.EpisodeAPI
import com.api.tvmaze.api.Network
import com.api.tvmaze.api.SearchAPI
import com.api.tvmaze.api.SeasonAPI
import com.api.tvmaze.api.ShowAPI
import com.api.tvmaze.model.Episode
import com.api.tvmaze.model.Search
import com.api.tvmaze.model.Season
import com.api.tvmaze.model.Show
import com.api.tvmaze.ui.Pagination
import com.api.tvmaze.ui.data.RealmManager
import com.api.tvmaze.ui.data.ShowObject
import io.realm.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowViewModel : ViewModel() {

    var currentSearchQuery: String? = ""

    private var _searchLiveDataList = MutableLiveData<List<Show>>()
    val searchLiveDataList: MutableLiveData<List<Show>>
        get() = _searchLiveDataList

    private val _seasonLiveDataList = MutableLiveData<List<Season>>()
    val seasonLiveDataList: MutableLiveData<List<Season>>
        get() = _seasonLiveDataList


    private var _episodeLiveDataList = MutableLiveData<List<Episode>>()
    val episodeLiveDataList: MutableLiveData<List<Episode>>
        get() = _episodeLiveDataList


    val showLiveData = MutableLiveData<Show>()
    val seasonLiveData = MutableLiveData<Season>()
    val episodeLiveData = MutableLiveData<Episode>()

    fun response(show: Show) {
        showLiveData.value = show
    }

    fun responseSeason(season: Season) {
        seasonLiveData.value = season
    }

    fun responseEpisode(episode: Episode) {
        episodeLiveData.value = episode
    }

    companion object {
        const val URL = "https://api.tvmaze.com"
    }

    private val retrofitClient = Network.retrofitConfig(URL)

    val _pagingData: MutableStateFlow<PagingData<Show>> = MutableStateFlow(PagingData.empty())
    val pagingData: Flow<PagingData<Show>> = _pagingData


    fun getShows() {
        val call = retrofitClient.create(ShowAPI::class.java)

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val pager = Pager(
                    config = PagingConfig(pageSize = 250),
                    pagingSourceFactory = { Pagination(call) }
                )
                pager
                    .flow
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        withContext(Dispatchers.Main) {
                            _pagingData.value = pagingData
                        }
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSeasons(id: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _seasonLiveDataList.postValue(
                    retrofitClient.create(SeasonAPI::class.java).getSeasonAPI(id).body()
                )
            } catch (e: Exception) {
                _seasonLiveDataList.postValue(ArrayList())
            }
        }
    }

    fun getEpisodes(seasonId: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _episodeLiveDataList.postValue(
                    retrofitClient.create(EpisodeAPI::class.java).getEpisodeAPI(seasonId).body()
                )
            } catch (e: Exception) {
                _episodeLiveDataList.postValue(ArrayList())
            }
        }
    }

    fun getSearch(path: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    retrofitClient.create(SearchAPI::class.java).getShowSearchAPI(path).body()

                _searchLiveDataList.postValue(
                    Search.mapper(response)
                )
            } catch (e: Exception) {
                _searchLiveDataList.postValue(ArrayList())
            }
        }
    }

    fun getFavoriteShow(): List<ShowObject> {
        val realm = RealmManager.getRealmInstance()

        return try {
            val realmResults: RealmResults<ShowObject> =
                realm.where(ShowObject::class.java).findAll()
            realm.copyFromRealm(realmResults) ?: emptyList()
        } finally {
            RealmManager.closeRealm()
        }
    }

    fun saveFavoriteShow(show: Show) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val realm = RealmManager.getRealmInstance()
                try {
                    realm.executeTransaction { realmTransaction ->
                        val showObject = Show.mapperShowObject(show)
                        realmTransaction.insert(showObject)
                    }
                } finally {
                    RealmManager.closeRealm()
                }
            }
        }
    }

    fun deleteFavoriteShow(show: Show): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        viewModelScope.launch(Dispatchers.IO) {
            val realm = RealmManager.getRealmInstance()
            try {
                realm.executeTransaction { realmTransaction ->
                    val showObject = Show.mapperShowObject(show)
                    val result = realmTransaction.where(ShowObject::class.java)
                        .equalTo("id", showObject.id)
                        .findFirst()
                    result?.deleteFromRealm()
                    liveData.postValue(true)
                }
            } finally {
                RealmManager.closeRealm()
            }
        }

        return liveData
    }

    fun checkIfIsFavorite(showId: Int?): Boolean {
        val realm = RealmManager.getRealmInstance()

        try {
            val result = showId?.let {
                realm.where(ShowObject::class.java)
                    .equalTo("id", showId)
                    .findFirst()
            }
            return result != null
        } finally {
            RealmManager.closeRealm()
        }
    }


}