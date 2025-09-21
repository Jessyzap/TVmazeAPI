package com.api.tvmaze.features.favorite_show.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.api.tvmaze.features.favorite_show.domain.IFavoriteShowRepository
import com.api.tvmaze.features.show.domain.entity.ShowEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteShowViewModel @Inject constructor(
    private val repository: IFavoriteShowRepository
) : ViewModel() {

    fun getFavoriteShow(): LiveData<List<ShowEntity>> = repository.getFavoriteShows()

    fun deleteFavoriteShow(show: ShowEntity) = repository.deleteFavoriteShow(show)

    override fun onCleared() {
        super.onCleared()
        repository.closeRealm()
    }

}