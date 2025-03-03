package com.api.tvmaze.features.show.data.model

import android.os.Parcelable
import com.api.tvmaze.features.show.data.model.ImageType
import com.api.tvmaze.features.show.data.model.ScheduleType
import com.api.tvmaze.utils.ID
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
) : Parcelable, ID {
    override val objId: Int
        get() = id

}