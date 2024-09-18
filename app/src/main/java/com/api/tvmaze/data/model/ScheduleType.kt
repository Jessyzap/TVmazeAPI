package com.api.tvmaze.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleType(
    val time: String,
    val days: List<String>
) : Parcelable {
    fun scheduleDetail(): String {
        return "Day: ${days.joinToString(separator = ", ")} \nTime: $time"
    }
}