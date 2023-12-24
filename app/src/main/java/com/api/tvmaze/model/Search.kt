package com.api.tvmaze.model

import java.io.Serializable

data class Search(
    val show: Show
) : Serializable {
    companion object {
        fun mapper(response: List<Search>?): List<Show>? {
           return response?.map {
                Show(
                    id = it.show.id,
                    genres = it.show.genres,
                    schedule = ScheduleType(
                        time = it.show.schedule.time,
                        days = it.show.schedule.days
                    ),
                    image = it.show.image,
                    name = it.show.name,
                    summary = it.show.summary
                )
            }
        }
    }
}