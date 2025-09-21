package com.api.tvmaze.features.show.data.model

import android.os.Parcelable
import com.api.tvmaze.features.show.domain.entity.SeasonEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Season(
    val id: Int,
    val number: Int
) : Parcelable {

    fun toEntity() = SeasonEntity(
        id = id,
        seasonDetail = "Season $number"
    )

}