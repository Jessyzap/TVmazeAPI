package com.api.tvmaze.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.api.tvmaze.api.ShowAPI
import com.api.tvmaze.model.Show

class Pagination(private val api: ShowAPI) : PagingSource<Int, Show>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Show> {
        return try {
            val nextPageNumber = params.key ?: 0
            val response = api.getShowAPI(nextPageNumber)

            if (response.isSuccessful) {
                val shows = response.body() ?: emptyList()

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

    override fun getRefreshKey(state: PagingState<Int, Show>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}