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
import com.api.tvmaze.features.favorite_show.data.model.ShowObject
import com.api.tvmaze.features.show.data.datasource.service.ShowAPI
import com.api.tvmaze.features.show.data.model.Episode
import com.api.tvmaze.features.show.data.model.Search
import com.api.tvmaze.features.show.data.model.Season
import com.api.tvmaze.features.show.data.model.Show
import com.api.tvmaze.features.favorite_show.domain.IFavoriteShowRepository
import com.api.tvmaze.features.show.domain.IShowRepository
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
    private val favoriteRepository: IFavoriteShowRepository
) : ViewModel() {

    private var searchJob: Job? = null
    private var currentSearchQuery: String? = ""

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

    fun setShow(show: Show) {
        _showLiveData.value = show
    }

    fun setSeason(season: Season) {
        _seasonLiveData.value = season
    }

    fun setEpisode(episode: Episode) {
        _episodeLiveData.value = episode
    }

    private val _pagingData: MutableStateFlow<PagingData<Show>> =
        MutableStateFlow(PagingData.empty())
    val pagingData: Flow<PagingData<Show>> = _pagingData


    fun getShows() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val pager = Pager(
                    config = PagingConfig(pageSize = 250),
                    pagingSourceFactory = { Pagination(ShowAPI::class.java) }
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
                    is ResponseWrapper.SuccessResult<List<Season>> -> {
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
                    is ResponseWrapper.SuccessResult<List<Episode>> -> {
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
                    is ResponseWrapper.SuccessResult<List<Search>> -> {
                        _searchLiveDataList.postValue(Search.mapper(response.result))
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

    fun saveFavoriteShow(show: Show) =
        favoriteRepository.saveFavoriteShow(ShowObject.mapperShowObject(show))

    fun deleteFavoriteShow(show: Show) =
        favoriteRepository.deleteFavoriteShow(ShowObject.mapperShowObject(show))

    fun checkIfIsFavorite(showId: Int?) = favoriteRepository.checkIfIsFavorite(showId)

    fun clearSearch() {
        _searchLiveDataList.value = null
    }

}