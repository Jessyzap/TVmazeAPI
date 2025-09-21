package com.api.tvmaze.features.favorite_show.data.repository

import androidx.lifecycle.map
import com.api.tvmaze.features.favorite_show.data.datasource.local.IFavoriteShowLocalDataSource
import com.api.tvmaze.features.favorite_show.domain.IFavoriteShowRepository
import com.api.tvmaze.features.favorite_show.data.model.ShowObject
import com.api.tvmaze.features.show.domain.entity.ShowEntity
import javax.inject.Inject

class FavoriteShowRepositoryImpl @Inject constructor(
    private val favoriteShowLocalDataSource: IFavoriteShowLocalDataSource,
) : IFavoriteShowRepository {

    override fun getFavoriteShows() = favoriteShowLocalDataSource.getFavoriteShows().map { list ->
        list.map { ShowObject.mapperShow(it) }
    }

    override fun saveFavoriteShow(show: ShowEntity) =
        favoriteShowLocalDataSource.saveFavoriteShow(ShowObject.mapperShowObject(show))

    override fun deleteFavoriteShow(show: ShowEntity) =
        favoriteShowLocalDataSource.deleteFavoriteShow(ShowObject.mapperShowObject(show))

    override fun checkIfIsFavorite(showId: Int?) =
        favoriteShowLocalDataSource.checkIfIsFavorite(showId)

    override fun closeRealm() = favoriteShowLocalDataSource.closeRealm()

}