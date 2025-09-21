package com.api.tvmaze.features.show.domain.entity

import android.os.Parcelable
import com.api.tvmaze.utils.DiffIdentifiable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShowEntity(
    val id: Int,
    val genres: List<String>,
    val schedule: ScheduleTypeEntity,
    val image: ImageTypeEntity?,
    val name: String,
    val summary: String?,
    var isFavorite: Boolean = false
) : Parcelable, DiffIdentifiable {
    override val diffId: Int
        get() = id

    @Parcelize
    data class ScheduleTypeEntity(
        val scheduleDetail: String,
        val time: String,
        val days: List<String>
    ) : Parcelable

}