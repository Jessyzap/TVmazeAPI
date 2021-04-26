package com.api.tvmaze.model

import com.google.gson.annotations.SerializedName

class ScheduleType(

    @SerializedName("time") val time: String,
    @SerializedName("days") val days: Array<String>

) {
    fun scheduleDetail(): String {
        return "Day: ${days.joinToString(separator = ", ")} \nTime: $time"
    }
}