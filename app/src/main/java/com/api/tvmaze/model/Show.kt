package com.api.tvmaze.model

import com.google.gson.annotations.SerializedName

data class Show(
        @SerializedName("id") val id: Int,
        @SerializedName("genres") val genres: Array<String>,
        @SerializedName("schedule") val schedule: ScheduleType,
        @SerializedName("image") val image: ImageType?,
        @SerializedName("name") val name: String,
        @SerializedName("summary") val summary: String
)




