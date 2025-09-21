package com.api.tvmaze.features.show.data.model

import android.os.Parcelable
import com.api.tvmaze.features.show.domain.entity.ImageTypeEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageType(
    val medium: String?,
    val original: String?
) : Parcelable {

    fun toEntity() = ImageTypeEntity(
        medium = medium,
        original = original
    )

}