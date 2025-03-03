package com.api.tvmaze.features.favorite_show.data.repository

import com.api.tvmaze.features.favorite_show.data.datasource.local.IFavoriteShowLocalDataSource
import com.api.tvmaze.features.favorite_show.domain.IFavoriteShowRepository
import com.api.tvmaze.features.favorite_show.data.model.ShowObject
import javax.inject.Inject

class FavoriteShowRepositoryImpl @Inject constructor(
    private val showLocalDataSource: IFavoriteShowLocalDataSource,
) : IFavoriteShowRepository {

    override fun getFavoriteShows() = showLocalDataSource.getFavoriteShows()

    override fun saveFavoriteShow(show: ShowObject) = showLocalDataSource.saveFavoriteShow(show)

    override fun deleteFavoriteShow(show: ShowObject) = showLocalDataSource.deleteFavoriteShow(show)

    override fun checkIfIsFavorite(showId: Int?) = showLocalDataSource.checkIfIsFavorite(showId)

}