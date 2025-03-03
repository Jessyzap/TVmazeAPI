package com.api.tvmaze.features.favorite_show.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.api.tvmaze.features.favorite_show.domain.IFavoriteShowRepository
import com.api.tvmaze.features.favorite_show.data.model.ShowObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteShowViewModel @Inject constructor(
    private val repository: IFavoriteShowRepository
) : ViewModel() {

    fun getFavoriteShow(): LiveData<List<ShowObject>> = repository.getFavoriteShows()

    fun deleteFavoriteShow(show: ShowObject) = repository.deleteFavoriteShow(show)

    override fun onCleared() {
        super.onCleared()
        repository.closeRealm()
    }

}