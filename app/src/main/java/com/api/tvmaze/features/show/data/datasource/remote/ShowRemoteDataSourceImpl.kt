package com.api.tvmaze.features.show.data.datasource.remote

import com.api.tvmaze.core.network.NetworkRequestHandler
import com.api.tvmaze.core.network.ResponseWrapper
import com.api.tvmaze.core.network.retrofitConfig
import com.api.tvmaze.features.show.data.datasource.service.EpisodeAPI
import com.api.tvmaze.features.show.data.datasource.service.SearchAPI
import com.api.tvmaze.features.show.data.datasource.service.SeasonAPI
import com.api.tvmaze.features.show.data.datasource.service.ShowAPI
import com.api.tvmaze.features.show.data.model.Episode
import com.api.tvmaze.features.show.data.model.Search
import com.api.tvmaze.features.show.data.model.Season
import com.api.tvmaze.features.show.data.model.Show

class ShowRemoteDataSourceImpl : IShowRemoteDataSource {

    override suspend fun getShows(page: Int): ResponseWrapper<List<Show>> {
        return NetworkRequestHandler.doRequest {
            retrofitConfig.create(ShowAPI::class.java).getShowAPI(page)
        }
    }

    override suspend fun getSeasons(id: Int): ResponseWrapper<List<Season>> {
        return NetworkRequestHandler.doRequest {
            retrofitConfig.create(SeasonAPI::class.java).getSeasonAPI(id)
        }
    }

    override suspend fun getEpisodes(seasonId: Int): ResponseWrapper<List<Episode>> {
        return NetworkRequestHandler.doRequest {
            retrofitConfig.create(EpisodeAPI::class.java).getEpisodeAPI(seasonId)
        }
    }

    override suspend fun getSearch(path: String): ResponseWrapper<List<Search>> {
        return NetworkRequestHandler.doRequest {
            retrofitConfig.create(SearchAPI::class.java).getShowSearchAPI(path)
        }
    }

}