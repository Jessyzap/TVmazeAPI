package com.api.tvmaze.data.model

import android.os.Parcelable
import com.api.tvmaze.utils.ID
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Season(
    @SerializedName("id") val id: Int,
    @SerializedName("number") val number: Int,

    ) : Parcelable, ID {
    override val objId: Int
        get() = id

    fun seasonDetail(): String {
        return "Season $number"
    }
}