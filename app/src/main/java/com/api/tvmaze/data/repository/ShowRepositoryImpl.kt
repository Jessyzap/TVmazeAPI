package com.api.tvmaze.data.repository

import com.api.tvmaze.data.datasource.local.IShowLocalDataSource
import com.api.tvmaze.data.datasource.remote.IShowRemoteDataSource
import com.api.tvmaze.data.model.Show
import com.api.tvmaze.domain.IShowRepository
import javax.inject.Inject

class ShowRepositoryImp @Inject constructor(
    private val showLocalDataSource: IShowLocalDataSource,
    private val showRemoteDataSource: IShowRemoteDataSource
) : IShowRepository {

    override fun getFavoriteShows() = showLocalDataSource.getFavoriteShows()

    override fun saveFavoriteShow(show: Show) = showLocalDataSource.saveFavoriteShow(show)

    override fun deleteFavoriteShow(show: Show) = showLocalDataSource.deleteFavoriteShow(show)

    override fun checkIfIsFavorite(showId: Int?) = showLocalDataSource.checkIfIsFavorite(showId)

    override suspend fun getShows(page: Int) = showRemoteDataSource.getShows(page)

    override suspend fun getSeasons(id: Int) = showRemoteDataSource.getSeasons(id)

    override suspend fun getEpisodes(seasonId: Int) = showRemoteDataSource.getEpisodes(seasonId)

    override suspend fun getSearch(path: String) = showRemoteDataSource.getSearch(path)

}