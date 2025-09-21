package com.api.tvmaze.features.show.data.model

import android.os.Parcelable
import com.api.tvmaze.features.show.domain.entity.ShowEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleType(
    val time: String,
    val days: List<String>
) : Parcelable {

    fun toShowEntity() = ShowEntity.ScheduleTypeEntity(
        scheduleDetail = "Day: ${days.joinToString(separator = ", ")} \nTime: $time",
        time = time,
        days = days
    )

}