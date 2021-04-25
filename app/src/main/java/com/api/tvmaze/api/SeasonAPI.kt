package com.api.tvmaze.api

import com.api.tvmaze.model.Season
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SeasonAPI {

    @GET("/shows/{ShowID}/seasons")

    fun getSeasonAPI(
        @Path("ShowID") id: Int
    ): Call<List<Season>>
}