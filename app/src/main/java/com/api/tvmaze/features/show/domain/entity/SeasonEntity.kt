package com.api.tvmaze.features.show.domain.entity

import android.os.Parcelable
import com.api.tvmaze.utils.DiffIdentifiable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SeasonEntity(
    val id: Int,
    val seasonDetail: String
) : Parcelable, DiffIdentifiable {
    override val diffId: Int
        get() = id
}