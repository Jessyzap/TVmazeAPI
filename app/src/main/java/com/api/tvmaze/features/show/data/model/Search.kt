package com.api.tvmaze.features.show.data.model

import android.os.Parcelable
import com.api.tvmaze.features.show.domain.entity.ImageTypeEntity
import com.api.tvmaze.features.show.domain.entity.ShowEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Search(
    val show: Show
) : Parcelable {

    fun toShowEntity(): ShowEntity {
        return ShowEntity(
                id = show.id,
                genres = show.genres,
                schedule = show.schedule.toShowEntity(),
                image = ImageTypeEntity(
                    medium = show.image?.medium,
                    original = show.image?.original
                ),
                name = show.name,
                summary = show.summary
            )
    }

}