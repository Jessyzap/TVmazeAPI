package com.api.tvmaze.features.show.domain

import com.api.tvmaze.core.network.ResponseWrapper
import com.api.tvmaze.features.show.data.model.Episode
import com.api.tvmaze.features.show.data.model.Search
import com.api.tvmaze.features.show.data.model.Season
import com.api.tvmaze.features.show.data.model.Show

interface IShowRepository {

    suspend fun getShows(page: Int): ResponseWrapper<List<Show>>

    suspend fun getSeasons(id: Int): ResponseWrapper<List<Season>>

    suspend fun getEpisodes(seasonId: Int): ResponseWrapper<List<Episode>>

    suspend fun getSearch(path: String): ResponseWrapper<List<Search>>

}