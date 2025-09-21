package com.api.tvmaze.features.favorite_show.domain

import androidx.lifecycle.LiveData
import com.api.tvmaze.features.show.domain.entity.ShowEntity

interface IFavoriteShowRepository {

    fun getFavoriteShows(): LiveData<List<ShowEntity>>

    fun saveFavoriteShow(show: ShowEntity)

    fun deleteFavoriteShow(show: ShowEntity): LiveData<Boolean>

    fun checkIfIsFavorite(showId: Int?): LiveData<Boolean>

    fun closeRealm()
}