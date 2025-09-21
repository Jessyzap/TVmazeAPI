package com.api.tvmaze.features.show.presentation.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.api.tvmaze.features.show.data.datasource.service.ShowAPI
import com.api.tvmaze.features.show.domain.entity.ShowEntity

class Pagination(private val api: ShowAPI) : PagingSource<Int, ShowEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShowEntity> {
        return try {

            val nextPageNumber = params.key ?: 0
            val response = api.getShowAPI(nextPageNumber)

            if (response.isSuccessful) {

                val shows = response.body()?.map { it.toEntity() } ?: emptyList()

                LoadResult.Page(
                    data = shows,
                    prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                    nextKey = if (shows.isEmpty()) null else nextPageNumber + 1
                )
            } else {
                LoadResult.Error(Exception())
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ShowEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}