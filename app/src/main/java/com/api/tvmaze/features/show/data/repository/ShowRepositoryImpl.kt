package com.api.tvmaze.features.show.data.repository

import com.api.tvmaze.core.network.ResponseWrapper
import com.api.tvmaze.features.show.data.datasource.remote.IShowRemoteDataSource
import com.api.tvmaze.features.show.data.model.Episode
import com.api.tvmaze.features.show.data.model.Search
import com.api.tvmaze.features.show.data.model.Season
import com.api.tvmaze.features.show.data.model.Show
import com.api.tvmaze.features.show.domain.IShowRepository
import com.api.tvmaze.features.show.domain.entity.EpisodeEntity
import com.api.tvmaze.features.show.domain.entity.SeasonEntity
import com.api.tvmaze.features.show.domain.entity.ShowEntity
import javax.inject.Inject

class ShowRepositoryImp @Inject constructor(
    private val showRemoteDataSource: IShowRemoteDataSource
) : IShowRepository {
    override suspend fun getShows(page: Int): ResponseWrapper<List<ShowEntity>> {
        return when (val response = showRemoteDataSource.getShows(page)) {
            is ResponseWrapper.SuccessResult<List<Show>> ->
                ResponseWrapper.SuccessResult(response.result.map { it.toEntity() })

            is ResponseWrapper.ErrorResult<List<Show>> -> ResponseWrapper.ErrorResult(response.message)
        }
    }

    override suspend fun getSeasons(id: Int): ResponseWrapper<List<SeasonEntity>> {
        return when (val response = showRemoteDataSource.getSeasons(id)) {
            is ResponseWrapper.SuccessResult<List<Season>> ->
                ResponseWrapper.SuccessResult(response.result.map { it.toEntity() })

            is ResponseWrapper.ErrorResult<List<Season>> -> ResponseWrapper.ErrorResult(response.message)
        }
    }

    override suspend fun getEpisodes(seasonId: Int): ResponseWrapper<List<EpisodeEntity>> {
        return when (val response = showRemoteDataSource.getEpisodes(seasonId)) {
            is ResponseWrapper.SuccessResult<List<Episode>> ->
                ResponseWrapper.SuccessResult(response.result.map { it.toEntity() })

            is ResponseWrapper.ErrorResult<List<Episode>> -> ResponseWrapper.ErrorResult(response.message)
        }
    }

    override suspend fun getSearch(path: String): ResponseWrapper<List<ShowEntity>> {
        return when (val response = showRemoteDataSource.getSearch(path)) {
            is ResponseWrapper.SuccessResult<List<Search>> ->
                ResponseWrapper.SuccessResult(response.result.map { it.toShowEntity() })

            is ResponseWrapper.ErrorResult<List<Search>> -> ResponseWrapper.ErrorResult(response.message)
        }
    }

}