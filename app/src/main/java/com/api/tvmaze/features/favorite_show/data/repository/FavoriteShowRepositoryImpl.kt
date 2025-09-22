package com.api.tvmaze.features.favorite_show.data.repository

import com.api.tvmaze.features.favorite_show.data.datasource.local.IFavoriteShowLocalDataSource
import com.api.tvmaze.features.favorite_show.data.model.ShowObject
import com.api.tvmaze.features.favorite_show.domain.IFavoriteShowRepository
import com.api.tvmaze.features.show.domain.entity.ShowEntity
import javax.inject.Inject

class FavoriteShowRepositoryImpl @Inject constructor(
    private val favoriteShowLocalDataSource: IFavoriteShowLocalDataSource,
) : IFavoriteShowRepository {

    override suspend fun getFavoriteShows() =
        favoriteShowLocalDataSource.getFavoriteShows().map { showObject ->
            ShowObject.mapperShow(showObject)
        }

    override suspend fun saveFavoriteShow(show: ShowEntity) =
        favoriteShowLocalDataSource.saveFavoriteShow(ShowObject.mapperShowObject(show))

    override suspend fun deleteFavoriteShow(show: ShowEntity) =
        favoriteShowLocalDataSource.deleteFavoriteShow(ShowObject.mapperShowObject(show))

    override suspend fun checkIfIsFavorite(showId: Int?) =
        favoriteShowLocalDataSource.checkIfIsFavorite(showId)

    override fun closeRealm() = favoriteShowLocalDataSource.closeRealm()

}