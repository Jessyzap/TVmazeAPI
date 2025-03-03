package com.api.tvmaze.features.show.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageType(
    val medium: String?,
    val original: String?
) : Parcelable