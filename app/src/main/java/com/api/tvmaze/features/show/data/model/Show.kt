package com.api.tvmaze.features.show.data.model

import android.os.Parcelable
import com.api.tvmaze.features.show.domain.entity.ShowEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Show(
    val id: Int,
    val genres: List<String>,
    val schedule: ScheduleType,
    val image: ImageType?,
    val name: String,
    val summary: String?,
) : Parcelable {

    fun toEntity() = ShowEntity(
        id = id,
        genres = genres,
        schedule = schedule.toShowEntity(),
        image = image?.toEntity(),
        name = name,
        summary = summary
    )

}