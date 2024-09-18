package com.api.tvmaze.data.datasource.service

import com.api.tvmaze.data.model.Season
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SeasonAPI {

    @GET("/shows/{ShowID}/seasons")

    suspend fun getSeasonAPI(
        @Path("ShowID") id: Int
    ): Response <List<Season>>
}