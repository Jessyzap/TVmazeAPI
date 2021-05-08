package com.api.tvmaze.model

import com.google.gson.annotations.SerializedName

data class Show(
        @SerializedName("id") val id: Int,
        @SerializedName("genres") val genre: Array<String>,
        @SerializedName("schedule") val schedule: ScheduleType,
        @SerializedName("image") val image: ImageType?,
        @SerializedName("name") val title: String,
        @SerializedName("summary") val description: String
)




