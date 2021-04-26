package com.api.tvmaze.model

import com.google.gson.annotations.SerializedName

data class Season (
    @SerializedName("id") val id: Int,
    @SerializedName("number") val number: Int
) {

    fun seasonDetail(): String {
        return "Season $number"
    }
}