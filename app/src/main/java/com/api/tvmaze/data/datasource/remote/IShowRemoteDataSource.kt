package com.api.tvmaze.data.datasource.remote

import com.api.tvmaze.core.network.ResponseWrapper
import com.api.tvmaze.data.model.Episode
import com.api.tvmaze.data.model.Search
import com.api.tvmaze.data.model.Season
import com.api.tvmaze.data.model.Show

interface IShowRemoteDataSource {

    suspend fun getShows(page: Int): ResponseWrapper<List<Show>>

    suspend fun getSeasons(id: Int): ResponseWrapper<List<Season>>

    suspend fun getEpisodes(seasonId: Int): ResponseWrapper<List<Episode>>

    suspend fun getSearch(path: String): ResponseWrapper<List<Search>>

}