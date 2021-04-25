package com.api.tvmaze.api

import com.api.tvmaze.model.Episode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeAPI {

    @GET("/seasons/{SeasonID}/episodes")

    fun getEpisodeAPI(
        @Path("SeasonID") id: Int
    ): Call<List<Episode>>
}