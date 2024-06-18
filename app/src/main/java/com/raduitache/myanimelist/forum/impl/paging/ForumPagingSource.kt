package com.raduitache.myanimelist.forum.impl.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.raduitache.myanimelist.forum.models.Data
import com.raduitache.myanimelist.services.forum.ForumService
import retrofit2.await

class ForumPagingSource(
    private val forumService: ForumService,
    private val boardId: Int,
) : PagingSource<Int, Data>() {
    override fun getRefreshKey(state: PagingState<Int, Data>): Int? = state.anchorPosition?.let {
        state.closestPageToPosition(it)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        val seasonalAnime = forumService.getBoardData(
            boardId = boardId,
            page = params.key?.let { it * params.loadSize } ?: 0,
        ).await()

        return LoadResult.Page(
            data = seasonalAnime.data,
            prevKey = seasonalAnime.paging.previous?.let { params.key?.dec() },
            nextKey = seasonalAnime.paging.next?.let { params.key?.inc() },
        )
    }
}