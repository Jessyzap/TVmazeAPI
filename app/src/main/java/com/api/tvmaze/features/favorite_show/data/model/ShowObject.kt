package com.api.tvmaze.features.favorite_show.data.model

import com.api.tvmaze.features.show.domain.entity.ImageTypeEntity
import com.api.tvmaze.features.show.domain.entity.ShowEntity
import com.api.tvmaze.utils.DiffIdentifiable
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
) : RealmObject(), DiffIdentifiable {

    override val diffId: Int
        get() = id

    companion object {
        fun mapperShow(showObject: ShowObject): ShowEntity {
            return ShowEntity(
                id = showObject.id,
                genres = showObject.genres,
                schedule = ShowEntity.ScheduleTypeEntity(
                    scheduleDetail = showObject.schedule?.scheduleDetail.orEmpty(),
                    time = showObject.schedule?.time.orEmpty(),
                    days = showObject.schedule?.days.orEmpty()
                ),
                image = ImageTypeEntity(
                    medium = showObject.image?.medium,
                    original = showObject.image?.original
                ),
                name = showObject.name,
                summary = showObject.summary
            )
        }

        fun mapperShowObject(showEntity: ShowEntity): ShowObject {
            return ShowObject(
                id = showEntity.id,
                genres = RealmList<String>().apply {
                    addAll(showEntity.genres)
                },
                schedule = ScheduleTypeObject(
                    scheduleDetail = showEntity.schedule.scheduleDetail
                ),
                image = ImageTypeObject(
                    medium = showEntity.image?.medium,
                    original = showEntity.image?.original
                ),
                name = showEntity.name,
                summary = showEntity.summary
            )
        }
    }

}

open class ScheduleTypeObject(
    var scheduleDetail: String = "",
    var time: String? = null,
    var days: RealmList<String> = RealmList()
) : RealmObject()

open class ImageTypeObject(
    var medium: String? = null,
    var original: String? = null
) : RealmObject()
