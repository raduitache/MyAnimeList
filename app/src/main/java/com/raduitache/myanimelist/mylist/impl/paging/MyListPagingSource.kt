package com.raduitache.myanimelist.mylist.impl.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.raduitache.myanimelist.responses.Anime
import com.raduitache.myanimelist.responses.Response
import com.raduitache.myanimelist.services.mylist.MyListService
import retrofit2.await

class MyListPagingSource(
    private val service: MyListService
) : PagingSource<Int, Response.DataItem<Anime>>() {
    override fun getRefreshKey(state: PagingState<Int, Response.DataItem<Anime>>): Int? =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Response.DataItem<Anime>> {
        val seasonalAnime = service.getMyList(
            page = params.key?.let { it * params.loadSize } ?: 0
        ).await()

        return LoadResult.Page(
            data = seasonalAnime.data,
            prevKey = seasonalAnime.paging.previous?.let { params.key?.dec() },
            nextKey = seasonalAnime.paging.next?.let { params.key?.inc() },
        )
    }
}