package com.api.tvmaze.features.show.data.datasource.remote

import com.api.tvmaze.core.network.NetworkRequestHandler
import com.api.tvmaze.core.network.ResponseWrapper
import com.api.tvmaze.features.show.data.datasource.service.EpisodeAPI
import com.api.tvmaze.features.show.data.datasource.service.SearchAPI
import com.api.tvmaze.features.show.data.datasource.service.SeasonAPI
import com.api.tvmaze.features.show.data.datasource.service.ShowAPI
import com.api.tvmaze.features.show.data.model.Episode
import com.api.tvmaze.features.show.data.model.Search
import com.api.tvmaze.features.show.data.model.Season
import com.api.tvmaze.features.show.data.model.Show

class ShowRemoteDataSourceImpl(
    val showAPI: ShowAPI,
    val seasonAPI: SeasonAPI,
    val episodeAPI: EpisodeAPI,
    val searchAPI: SearchAPI
) : IShowRemoteDataSource {

    override suspend fun getShows(page: Int): ResponseWrapper<List<Show>> {
        return NetworkRequestHandler.doRequest {
            showAPI.getShowAPI(page)
        }
    }

    override suspend fun getSeasons(id: Int): ResponseWrapper<List<Season>> {
        return NetworkRequestHandler.doRequest {
            seasonAPI.getSeasonAPI(id)
        }
    }

    override suspend fun getEpisodes(seasonId: Int): ResponseWrapper<List<Episode>> {
        return NetworkRequestHandler.doRequest {
            episodeAPI.getEpisodeAPI(seasonId)
        }
    }

    override suspend fun getSearch(path: String): ResponseWrapper<List<Search>> {
        return NetworkRequestHandler.doRequest {
            searchAPI.getShowSearchAPI(path)
        }
    }

}