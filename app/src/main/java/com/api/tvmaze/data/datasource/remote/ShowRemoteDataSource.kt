package com.api.tvmaze.data.datasource.remote

import com.api.tvmaze.core.network.NetworkRequestHandler
import com.api.tvmaze.core.network.ResponseWrapper
import com.api.tvmaze.core.network.retrofitConfig
import com.api.tvmaze.data.datasource.service.EpisodeAPI
import com.api.tvmaze.data.datasource.service.SearchAPI
import com.api.tvmaze.data.datasource.service.SeasonAPI
import com.api.tvmaze.data.datasource.service.ShowAPI
import com.api.tvmaze.data.model.Episode
import com.api.tvmaze.data.model.Search
import com.api.tvmaze.data.model.Season
import com.api.tvmaze.data.model.Show

class ShowRemoteDataSource {

    suspend fun getShows(page: Int): ResponseWrapper<List<Show>> {
        return NetworkRequestHandler.doRequest {
            retrofitConfig.create(ShowAPI::class.java).getShowAPI(page)
        }
    }

    suspend fun getSeasons(id: Int): ResponseWrapper<List<Season>> {
        return NetworkRequestHandler.doRequest {
            retrofitConfig.create(SeasonAPI::class.java).getSeasonAPI(id)
        }
    }

    suspend fun getEpisodes(seasonId: Int): ResponseWrapper<List<Episode>> {
        return NetworkRequestHandler.doRequest {
            retrofitConfig.create(EpisodeAPI::class.java).getEpisodeAPI(seasonId)
        }
    }

    suspend fun getSearch(path: String): ResponseWrapper<List<Search>> {
        return NetworkRequestHandler.doRequest {
            retrofitConfig.create(SearchAPI::class.java).getShowSearchAPI(path)
        }
    }

}