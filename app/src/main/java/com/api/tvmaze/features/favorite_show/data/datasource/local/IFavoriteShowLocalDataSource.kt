package com.api.tvmaze.features.favorite_show.data.datasource.local

import androidx.lifecycle.LiveData
import com.api.tvmaze.features.favorite_show.data.model.ShowObject

interface IFavoriteShowLocalDataSource {

    suspend fun getFavoriteShows(): List<ShowObject>

    suspend fun saveFavoriteShow(show: ShowObject)

    suspend fun deleteFavoriteShow(show: ShowObject): Boolean

    suspend fun checkIfIsFavorite(showId: Int?): Boolean

    fun closeRealm()

}