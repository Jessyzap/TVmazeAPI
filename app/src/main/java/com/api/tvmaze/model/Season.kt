package com.api.tvmaze.model

import com.api.tvmaze.utils.ID
import com.google.gson.annotations.SerializedName

data class Season (
    @SerializedName("id") val id: Int,
    @SerializedName("number") val number: Int,

) : ID {
    override val objId: Int
        get() = id
    fun seasonDetail(): String {
        return "Season $number"
    }
}