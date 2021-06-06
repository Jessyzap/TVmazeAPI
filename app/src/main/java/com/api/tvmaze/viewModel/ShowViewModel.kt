package com.api.tvmaze.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.api.tvmaze.api.*
import com.api.tvmaze.model.Episode
import com.api.tvmaze.model.Search
import com.api.tvmaze.model.Season
import com.api.tvmaze.model.Show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowViewModel : ViewModel() {

    private var _searchLiveDataList = MutableLiveData<List<Search>>()
    val searchLiveDataList: MutableLiveData<List<Search>>
        get() = _searchLiveDataList


    private var _showLiveDataList = MutableLiveData<List<Show>>()
    val showLiveDataList: MutableLiveData<List<Show>>
        get() = _showLiveDataList


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

    val retrofitClient = Network.retrofitConfig(URL)

    fun getShows() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _showLiveDataList.postValue(
                    retrofitClient.create(ShowAPI::class.java).getShowAPI().body()
                )
            } catch (e: Exception) {
                _showLiveDataList.postValue(ArrayList())
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
                _searchLiveDataList.postValue(
                    retrofitClient.create(SearchAPI::class.java).getShowSearchAPI(path).body()
                )
            } catch (e: Exception) {
                _searchLiveDataList.postValue(ArrayList())
            }
        }
    }
}