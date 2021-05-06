package com.api.tvmaze.api

import com.api.tvmaze.model.Search
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPI {

    @GET("/search/shows")

    fun getShowSearchAPI(
        @Query("q") search: String
    ): Call<List<Search>>
}


