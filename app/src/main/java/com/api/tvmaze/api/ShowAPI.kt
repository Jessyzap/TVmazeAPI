package com.api.tvmaze.api

import com.api.tvmaze.model.Show
import retrofit2.Call
import retrofit2.http.GET

interface ShowAPI {
    @GET("/shows")
    fun getShowAPI(): Call<List<Show>>
}