package com.api.tvmaze.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

data class Show(
        @SerializedName("id") val id: Int,
        @SerializedName("genres") val genre: Array<String>,
       // @SerializedName("time") val time: String,
      //  @SerializedName("medium") val image: String,
        @SerializedName("name") val title: String,
        @SerializedName("summary") val description: String
)


