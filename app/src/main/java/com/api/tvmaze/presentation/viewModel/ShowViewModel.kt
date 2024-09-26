package com.api.tvmaze.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.api.tvmaze.data.datasource.service.EpisodeAPI
import com.api.tvmaze.core.Network
import com.api.tvmaze.data.datasource.service.SearchAPI
import com.api.tvmaze.data.datasource.service.SeasonAPI
import com.api.tvmaze.data.datasource.service.ShowAPI
import com.api.tvmaze.data.model.Episode
import com.api.tvmaze.data.model.Search
import com.api.tvmaze.data.model.Season
import com.api.tvmaze.data.model.Show
import com.api.tvmaze.presentation.Pagination
import com.api.tvmaze.data.datasource.local.ShowObject
import com.api.tvmaze.data.repository.ShowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowViewModel : ViewModel() {

    var currentSearchQuery: String? = ""
    private val repository = ShowRepository()

    private val _searchLiveDataList = MutableLiveData<List<Show>?>()
    val searchLiveDataList: LiveData<List<Show>?>
        get() = _searchLiveDataList

    private val _seasonLiveDataList = MutableLiveData<List<Season>>()
    val seasonLiveDataList: LiveData<List<Season>>
        get() = _seasonLiveDataList

    private val _episodeLiveDataList = MutableLiveData<List<Episode>>()
    val episodeLiveDataList: LiveData<List<Episode>>
        get() = _episodeLiveDataList

    private val _showLiveData = MutableLiveData<Show>()
    val showLiveData: LiveData<Show>
        get() = _showLiveData

    private val _seasonLiveData = MutableLiveData<Season>()
    val seasonLiveData: LiveData<Season>
        get() = _seasonLiveData

    private val _episodeLiveData = MutableLiveData<Episode>()
    val episodeLiveData: LiveData<Episode>
        get() = _episodeLiveData

    fun setShow(show: Show) {
        _showLiveData.value = show
    }

    fun setSeason(season: Season) {
        _seasonLiveData.value = season
    }

    fun setEpisode(episode: Episode) {
        _episodeLiveData.value = episode
    }

    companion object {
        const val URL = "https://api.tvmaze.com"
    }

    private val retrofitClient = Network.retrofitConfig(URL)

    private val _pagingData: MutableStateFlow<PagingData<Show>> =
        MutableStateFlow(PagingData.empty())
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

    fun getFavoriteShow(): List<ShowObject> = repository.getFavoriteShows()

    fun saveFavoriteShow(show: Show) = repository.saveFavoriteShow(show)

    fun deleteFavoriteShow(show: Show) = repository.deleteFavoriteShow(show)

    fun checkIfIsFavorite(showId: Int?) = repository.checkIfIsFavorite(showId)

    fun clearSearch() {
        _searchLiveDataList.value = null
    }

}