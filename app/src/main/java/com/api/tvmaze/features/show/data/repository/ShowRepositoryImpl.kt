package com.api.tvmaze.features.show.data.repository

import com.api.tvmaze.features.show.data.datasource.remote.IShowRemoteDataSource
import com.api.tvmaze.features.show.domain.IShowRepository
import javax.inject.Inject

class ShowRepositoryImp @Inject constructor(
    private val showRemoteDataSource: IShowRemoteDataSource
) : IShowRepository {

    override suspend fun getShows(page: Int) = showRemoteDataSource.getShows(page)

    override suspend fun getSeasons(id: Int) = showRemoteDataSource.getSeasons(id)

    override suspend fun getEpisodes(seasonId: Int) = showRemoteDataSource.getEpisodes(seasonId)

    override suspend fun getSearch(path: String) = showRemoteDataSource.getSearch(path)

}