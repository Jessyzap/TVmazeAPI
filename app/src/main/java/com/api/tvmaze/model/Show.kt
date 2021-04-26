package com.api.tvmaze.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

data class Show(
       // @SerializedName("show") val show: Show?,
        @SerializedName("id") val id: Int,
        @SerializedName("genres") val genre: Array<String>,
        @SerializedName("schedule") val schedule: ScheduleType,
        @SerializedName("image") val image: ImageType?,
        @SerializedName("name") val title: String,
        @SerializedName("summary") val description: String
)




