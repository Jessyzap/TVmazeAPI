package com.api.tvmaze.model

import com.api.tvmaze.ui.data.ImageTypeObject
import com.api.tvmaze.ui.data.ScheduleTypeObject
import com.api.tvmaze.ui.data.ShowObject
import com.api.tvmaze.utils.ID
import com.google.gson.annotations.SerializedName
import io.realm.RealmList

data class Show(
    @SerializedName("id") val id: Int,
    @SerializedName("genres") val genres: List<String>,
    @SerializedName("schedule") val schedule: ScheduleType,
    @SerializedName("image") val image: ImageType?,
    @SerializedName("name") val name: String,
    @SerializedName("summary") val summary: String?,
    @SerializedName("isFavorite") var isFavorite: Boolean = false
) : ID {
    override val objId: Int
        get() = id

    companion object {
        fun mapperShowObject(showModel: Show): ShowObject {
            return ShowObject(
                id = showModel.id,
                genres =  RealmList<String>().apply {
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