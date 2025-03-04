package com.api.tvmaze.features.show.data.model

import android.os.Parcelable
import com.api.tvmaze.utils.DiffIdentifiable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Show(
    val id: Int,
    val genres: List<String>,
    val schedule: ScheduleType,
    val image: ImageType?,
    val name: String,
    val summary: String?,
    var isFavorite: Boolean = false
) : Parcelable, DiffIdentifiable {
    override val diffId: Int
        get() = id

}