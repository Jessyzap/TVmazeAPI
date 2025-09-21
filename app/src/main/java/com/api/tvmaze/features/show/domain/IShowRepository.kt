package com.api.tvmaze.features.show.domain

import com.api.tvmaze.core.network.ResponseWrapper
import com.api.tvmaze.features.show.domain.entity.EpisodeEntity
import com.api.tvmaze.features.show.domain.entity.SeasonEntity
import com.api.tvmaze.features.show.domain.entity.ShowEntity

interface IShowRepository {

    suspend fun getShows(page: Int): ResponseWrapper<List<ShowEntity>>

    suspend fun getSeasons(id: Int): ResponseWrapper<List<SeasonEntity>>

    suspend fun getEpisodes(seasonId: Int): ResponseWrapper<List<EpisodeEntity>>

    suspend fun getSearch(path: String): ResponseWrapper<List<ShowEntity>>

}