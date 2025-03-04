package com.api.tvmaze.features.show.data.datasource.service

import com.api.tvmaze.features.show.data.model.Search
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPI {

    @GET("/search/shows")

    suspend fun getShowSearchAPI(
        @Query("q") search: String
    ): Response <List<Search>>
}