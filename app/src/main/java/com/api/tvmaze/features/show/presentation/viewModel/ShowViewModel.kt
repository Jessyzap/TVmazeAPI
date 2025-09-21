package com.api.tvmaze.features.show.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.api.tvmaze.core.network.ResponseWrapper
import com.api.tvmaze.features.favorite_show.domain.IFavoriteShowRepository
import com.api.tvmaze.features.show.data.datasource.service.ShowAPI
import com.api.tvmaze.features.show.domain.IShowRepository
import com.api.tvmaze.features.show.domain.entity.EpisodeEntity
import com.api.tvmaze.features.show.domain.entity.SeasonEntity
import com.api.tvmaze.features.show.domain.entity.ShowEntity
import com.api.tvmaze.features.show.presentation.pagination.Pagination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
    private val repository: IShowRepository,
    private val favoriteRepository: IFavoriteShowRepository,
    private val showAPI: ShowAPI
) : ViewModel() {

    private var searchJob: Job? = null
    val searchCurrentJob
        get() = searchJob
    private var currentSearchQuery: String? = ""
    val searchCurrentSearchQuery
        get() = currentSearchQuery

    private val _searchLiveDataList = MutableLiveData<List<ShowEntity>?>()
    val searchLiveDataList: LiveData<List<ShowEntity>?>
        get() = _searchLiveDataList

    private val _seasonLiveDataList = MutableLiveData<List<SeasonEntity>>()
    val seasonLiveDataList: LiveData<List<SeasonEntity>>
        get() = _seasonLiveDataList

    private val _episodeLiveDataList = MutableLiveData<List<EpisodeEntity>>()
    val episodeLiveDataList: LiveData<List<EpisodeEntity>>
        get() = _episodeLiveDataList

    private val _showLiveData = MutableLiveData<ShowEntity>()
    val showLiveData: LiveData<ShowEntity>
        get() = _showLiveData

    private val _seasonLiveData = MutableLiveData<SeasonEntity>()
    val seasonLiveData: LiveData<SeasonEntity>
        get() = _seasonLiveData

    private val _episodeLiveData = MutableLiveData<EpisodeEntity>()
    val episodeLiveData: LiveData<EpisodeEntity>
        get() = _episodeLiveData

    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()

        if (query.length >= 2) {
            searchJob = viewModelScope.launch {
                delay(800L)
                if (query != currentSearchQuery) {
                    currentSearchQuery = query
                    getSearch(query)
                }
            }
        }
    }

    fun setShow(show: ShowEntity) {
        _showLiveData.value = show
    }

    fun setSeason(season: SeasonEntity) {
        _seasonLiveData.value = season
    }

    fun setEpisode(episode: EpisodeEntity) {
        _episodeLiveData.value = episode
    }

    private val _pagingData: MutableStateFlow<PagingData<ShowEntity>> =
        MutableStateFlow(PagingData.empty())
    val pagingData: Flow<PagingData<ShowEntity>> = _pagingData


    fun getShows() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val pager = Pager(
                    config = PagingConfig(pageSize = 250),
                    pagingSourceFactory = { Pagination(showAPI) }
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
                when (val response = repository.getSeasons(id)) {
                    is ResponseWrapper.SuccessResult<List<SeasonEntity>> -> {
                        _seasonLiveDataList.postValue(response.result)
                    }

                    is ResponseWrapper.ErrorResult -> {
                        _seasonLiveDataList.postValue(ArrayList())
                    }
                }
            } catch (e: Exception) {
                _seasonLiveDataList.postValue(ArrayList())
            }
        }
    }

    fun getEpisodes(seasonId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val response = repository.getEpisodes(seasonId)) {
                    is ResponseWrapper.SuccessResult<List<EpisodeEntity>> -> {
                        _episodeLiveDataList.postValue(response.result)
                    }

                    is ResponseWrapper.ErrorResult -> {
                        _episodeLiveDataList.postValue(ArrayList())
                    }
                }
            } catch (e: Exception) {
                _episodeLiveDataList.postValue(ArrayList())
            }
        }
    }

    private fun getSearch(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val response = repository.getSearch(path)) {
                    is ResponseWrapper.SuccessResult<List<ShowEntity>> -> {
                        _searchLiveDataList.postValue(response.result)
                    }

                    is ResponseWrapper.ErrorResult -> {
                        _searchLiveDataList.postValue(ArrayList())
                    }
                }
            } catch (e: Exception) {
                _searchLiveDataList.postValue(ArrayList())
            }
        }
    }

    fun saveFavoriteShow(show: ShowEntity) =
        favoriteRepository.saveFavoriteShow(show)

    fun deleteFavoriteShow(show: ShowEntity) =
        favoriteRepository.deleteFavoriteShow(show)

    fun checkIfIsFavorite(showId: Int?) = favoriteRepository.checkIfIsFavorite(showId)

    fun clearSearch() {
        _searchLiveDataList.value = null
    }

    override fun onCleared() {
        super.onCleared()
        favoriteRepository.closeRealm()
    }

}