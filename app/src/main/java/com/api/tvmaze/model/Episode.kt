package com.api.tvmaze.model

import com.google.gson.annotations.SerializedName

data class Episode(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val title: String,
    //  @SerializedName("medium") val image: String,
    @SerializedName("season") val season: Int,
    @SerializedName("number") val episode: Int,
    @SerializedName("summary") val description: String
) {

    fun seasonComplete(): String {
        return "Season: $season"
    }

    fun episodeComplete(): String {
        return "Episode: $episode"
    }
}