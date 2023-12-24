package com.api.tvmaze.model

import com.api.tvmaze.utils.ID
import com.google.gson.annotations.SerializedName

data class Episode(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: ImageType?,
    @SerializedName("season") val season: Int,
    @SerializedName("number") val number: Int? = 0,
    @SerializedName("summary") val summary: String?
) : ID {
    override val objId: Int
        get() = id

    fun seasonComplete(): String {
        return "Season: $season"
    }

    fun episodeComplete(): String {
        return "Episode: $number"
    }

    fun seasonEpisode(): String {
        return "S: $season  E: $number  |  $name"
    }
}