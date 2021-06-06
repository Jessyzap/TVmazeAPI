package com.api.tvmaze.api

import com.api.tvmaze.model.Show
import retrofit2.Response
import retrofit2.http.GET

interface ShowAPI {
    @GET("/shows")
    suspend fun getShowAPI(): Response <List<Show>>
}