package com.api.tvmaze.data.model

import android.os.Parcelable
import com.api.tvmaze.data.datasource.local.ImageTypeObject
import com.api.tvmaze.data.datasource.local.ScheduleTypeObject
import com.api.tvmaze.data.datasource.local.ShowObject
import com.api.tvmaze.utils.ID
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
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

    companion object {
        fun mapperShowObject(showModel: Show): ShowObject {
            return ShowObject(
                id = showModel.id,
                genres = RealmList<String>().apply {
                    addAll(showModel.genres)
                },
                schedule = ScheduleTypeObject(
                    time = showModel.schedule.time,
                    days = RealmList<String>().apply {
                        addAll(showModel.schedule.days)
                    }
                ),
                image = ImageTypeObject(
                    medium = showModel.image?.medium,
                    original = showModel.image?.original
                ),
                name = showModel.name,
                summary = showModel.summary
            )
        }
    }

}