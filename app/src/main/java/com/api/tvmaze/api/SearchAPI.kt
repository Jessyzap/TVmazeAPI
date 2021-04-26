package com.api.tvmaze.api

import com.api.tvmaze.model.Search
import com.api.tvmaze.model.Show
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPI {

//    @GET("/search/shows?q={search}")
//
//    fun getShowSearchAPI(
//        @Path("search") search: String
//    ): Call<List<Show>>

    @GET("/search/shows")

    fun getShowSearchAPI(
        @Query("q") search: String
    ): Call<List<Show>>

//    @GET("/singlesearch/shows")
//
//    fun getShowSearchAPI(
//        @Query("q") search: String
//    ): Call<List<Show>>
}


