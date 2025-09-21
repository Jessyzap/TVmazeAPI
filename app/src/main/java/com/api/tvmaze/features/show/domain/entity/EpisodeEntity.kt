package com.api.tvmaze.features.show.domain.entity

import android.os.Parcelable
import com.api.tvmaze.utils.DiffIdentifiable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodeEntity(
    val id: Int,
    val name: String,
    val seasonEpisode: String,
    val image: ImageTypeEntity?,
    val seasonComplete: String,
    val episodeComplete: String,
    val summary: String?
) : Parcelable, DiffIdentifiable {
    override val diffId: Int
        get() = id
}