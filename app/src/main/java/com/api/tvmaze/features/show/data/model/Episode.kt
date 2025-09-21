package com.api.tvmaze.features.show.data.model

import android.os.Parcelable
import com.api.tvmaze.features.show.domain.entity.EpisodeEntity
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
) : Parcelable {

    fun toEntity(): EpisodeEntity {
        return EpisodeEntity(
            id = id,
            name = name,
            seasonEpisode = "S: $season  E: $number  |  $name",
            image = image?.toEntity(),
            seasonComplete = "Season: $season",
            episodeComplete = "Episode: $number",
            summary = summary
        )
    }

}