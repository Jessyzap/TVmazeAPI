package com.api.tvmaze.api

import com.api.tvmaze.model.Show
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowAPI {
    @GET("/shows")
    suspend fun getShowAPI(
       @Query("page") page: Int
    ): Response <List<Show>>
}