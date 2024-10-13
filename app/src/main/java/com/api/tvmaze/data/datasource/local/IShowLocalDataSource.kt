package com.api.tvmaze.data.datasource.local

import androidx.lifecycle.LiveData
import com.api.tvmaze.data.model.Show

interface IShowLocalDataSource {

    fun getFavoriteShows(): List<ShowObject>

    fun saveFavoriteShow(show: Show)

    fun deleteFavoriteShow(show: Show): LiveData<Boolean>

    fun checkIfIsFavorite(showId: Int?): Boolean

}