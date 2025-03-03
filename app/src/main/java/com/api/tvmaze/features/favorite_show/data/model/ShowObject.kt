package com.api.tvmaze.features.favorite_show.data.model

import com.api.tvmaze.features.show.data.model.ImageType
import com.api.tvmaze.features.show.data.model.ScheduleType
import com.api.tvmaze.features.show.data.model.Show
import com.api.tvmaze.utils.ID
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ShowObject(
    @PrimaryKey
    var id: Int = 0,
    var genres: RealmList<String> = RealmList(),
    var schedule: ScheduleTypeObject? = null,
    var image: ImageTypeObject? = null,
    var name: String = "",
    var summary: String? = null
) : RealmObject(), ID {

    override val objId: Int
        get() = id

    companion object {
        fun mapperShow(showModel: ShowObject): Show {
            return Show(
                id = showModel.id,
                genres = showModel.genres,
                schedule = ScheduleType(
                    time = showModel.schedule?.time.orEmpty(),
                    days = showModel.schedule?.days.orEmpty()
                ),
                image = ImageType(
                    medium = showModel.image?.medium,
                    original = showModel.image?.original
                ),
                name = showModel.name,
                summary = showModel.summary
            )
        }

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

open class ScheduleTypeObject(
    var time: String = "",
    var days: RealmList<String> = RealmList()
) : RealmObject() {
    fun scheduleDetail(): String {
        return "Day: ${days.joinToString(separator = ", ")} \nTime: $time"
    }
}

open class ImageTypeObject(
    var medium: String? = null,
    var original: String? = null
) : RealmObject()
