package com.api.tvmaze.features.show.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageTypeEntity(
    val medium: String?,
    val original: String?
) : Parcelable