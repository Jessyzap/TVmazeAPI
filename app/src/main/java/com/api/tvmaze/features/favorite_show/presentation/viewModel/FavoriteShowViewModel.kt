package com.api.tvmaze.features.favorite_show.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.api.tvmaze.features.favorite_show.domain.IFavoriteShowRepository
import com.api.tvmaze.features.show.domain.entity.ShowEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteShowViewModel @Inject constructor(
    private val repository: IFavoriteShowRepository
) : ViewModel() {

    private val _favoriteShows = MutableLiveData<List<ShowEntity>>()
    val favoriteShows: LiveData<List<ShowEntity>> = _favoriteShows

    fun getFavoriteShow() {
        viewModelScope.launch {
            val shows = repository.getFavoriteShows()
            _favoriteShows.value = shows
        }
    }

    fun deleteFavoriteShow(show: ShowEntity) {
        viewModelScope.launch {
            val deleted = repository.deleteFavoriteShow(show)
            if (deleted) {
                _favoriteShows.value = repository.getFavoriteShows()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.closeRealm()
    }
}
