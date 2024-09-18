package com.api.tvmaze.data.datasource.local

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