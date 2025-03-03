package com.api.tvmaze.features.favorite_show.data.repository

import com.api.tvmaze.features.favorite_show.data.datasource.local.IFavoriteShowLocalDataSource
import com.api.tvmaze.features.favorite_show.domain.IFavoriteShowRepository
import com.api.tvmaze.features.favorite_show.data.model.ShowObject
import javax.inject.Inject

class FavoriteShowRepositoryImpl @Inject constructor(
    private val favoriteShowLocalDataSource: IFavoriteShowLocalDataSource,
) : IFavoriteShowRepository {

    override fun getFavoriteShows() = favoriteShowLocalDataSource.getFavoriteShows()

    override fun saveFavoriteShow(show: ShowObject) = favoriteShowLocalDataSource.saveFavoriteShow(show)

    override fun deleteFavoriteShow(show: ShowObject) = favoriteShowLocalDataSource.deleteFavoriteShow(show)

    override fun checkIfIsFavorite(showId: Int?) = favoriteShowLocalDataSource.checkIfIsFavorite(showId)

    override fun closeRealm() = favoriteShowLocalDataSource.closeRealm()

}