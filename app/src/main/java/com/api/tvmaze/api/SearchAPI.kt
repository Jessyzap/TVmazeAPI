package com.api.tvmaze.api

import com.api.tvmaze.model.Search
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPI {

    @GET("/search/shows")

    suspend fun getShowSearchAPI(
        @Query("q") search: String
    ): Response <List<Search>>
}


