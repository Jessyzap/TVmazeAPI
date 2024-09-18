package com.api.tvmaze.data.datasource.service

import com.api.tvmaze.data.model.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeAPI {

    @GET("/seasons/{SeasonID}/episodes")

    suspend fun getEpisodeAPI(
        @Path("SeasonID") id: Int
    ): Response<List<Episode>>
}