package com.api.tvmaze.data.datasource.service

import com.api.tvmaze.data.model.Show
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowAPI {
    @GET("/shows")
    suspend fun getShowAPI(
       @Query("page") page: Int
    ): Response <List<Show>>
}