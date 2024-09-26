package com.api.tvmaze.data.repository

import com.api.tvmaze.data.datasource.local.ShowLocalDataSource
import com.api.tvmaze.data.datasource.remote.ShowRemoteDataSource
import com.api.tvmaze.data.model.Show

class ShowRepository {
    //todo inject
    private val showLocalDataSource = ShowLocalDataSource()
    private val showRemoteDataSource = ShowRemoteDataSource()

    fun getFavoriteShows() = showLocalDataSource.getFavoriteShows()

    fun saveFavoriteShow(show: Show) = showLocalDataSource.saveFavoriteShow(show)

    fun deleteFavoriteShow(show: Show) = showLocalDataSource.deleteFavoriteShow(show)

    fun checkIfIsFavorite(showId: Int?) = showLocalDataSource.checkIfIsFavorite(showId)

}
