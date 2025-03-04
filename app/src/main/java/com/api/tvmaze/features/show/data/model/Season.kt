package com.api.tvmaze.features.show.data.model

import android.os.Parcelable
import com.api.tvmaze.utils.DiffIdentifiable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Season(
    @SerializedName("id") val id: Int,
    @SerializedName("number") val number: Int,

    ) : Parcelable, DiffIdentifiable {
    override val diffId: Int
        get() = id

    fun seasonDetail(): String {
        return "Season $number"
    }
}