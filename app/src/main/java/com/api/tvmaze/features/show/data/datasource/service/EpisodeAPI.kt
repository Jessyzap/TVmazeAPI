package com.api.tvmaze.features.show.data.datasource.service

import com.api.tvmaze.features.show.data.model.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeAPI {

    @GET("/seasons/{SeasonID}/episodes")

    suspend fun getEpisodeAPI(
        @Path("SeasonID") id: Int
    ): Response<List<Episode>>
}