package com.api.tvmaze.features.favorite_show.data.datasource.local

import androidx.lifecycle.LiveData
import com.api.tvmaze.features.favorite_show.data.model.ShowObject

interface IFavoriteShowLocalDataSource {

    fun getFavoriteShows(): LiveData<List<ShowObject>>

    fun saveFavoriteShow(show: ShowObject)

    fun deleteFavoriteShow(show: ShowObject): LiveData<Boolean>

    fun checkIfIsFavorite(showId: Int?): LiveData<Boolean>

    fun closeRealm()

}