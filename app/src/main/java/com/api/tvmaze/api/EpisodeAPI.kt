package com.api.tvmaze.api

import com.api.tvmaze.model.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeAPI {

    @GET("/seasons/{SeasonID}/episodes")

    suspend fun getEpisodeAPI(
        @Path("SeasonID") id: Int
    ): Response<List<Episode>>
}