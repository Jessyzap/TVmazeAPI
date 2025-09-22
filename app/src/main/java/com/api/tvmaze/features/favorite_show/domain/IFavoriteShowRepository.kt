package com.api.tvmaze.features.favorite_show.domain

import androidx.lifecycle.LiveData
import com.api.tvmaze.features.show.domain.entity.ShowEntity

interface IFavoriteShowRepository {

    suspend fun getFavoriteShows():List<ShowEntity>

    suspend fun saveFavoriteShow(show: ShowEntity)

    suspend fun deleteFavoriteShow(show: ShowEntity): Boolean

    suspend fun checkIfIsFavorite(showId: Int?): Boolean

    fun closeRealm()
}