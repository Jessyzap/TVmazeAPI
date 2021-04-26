package com.api.tvmaze.model

import com.google.gson.annotations.SerializedName

class ImageType (
    @SerializedName("medium") val medium: String?,
    @SerializedName("original") val original: String?
)