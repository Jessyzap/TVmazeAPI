package com.api.tvmaze.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageType(
    val medium: String?,
    val original: String?
) : Parcelable