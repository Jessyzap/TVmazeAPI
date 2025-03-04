package com.api.tvmaze.features.show.data.model

import android.os.Parcelable
import com.api.tvmaze.utils.DiffIdentifiable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episode(
    val id: Int,
    val name: String,
    val image: ImageType?,
    val season: Int,
    val number: Int? = 0,
    val summary: String?
) : Parcelable, DiffIdentifiable {
    override val diffId: Int
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