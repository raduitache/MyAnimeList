package com.raduitache.myanimelist.seasonal.impl.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.raduitache.myanimelist.responses.Anime
import com.raduitache.myanimelist.responses.Sorting
import com.raduitache.myanimelist.seasonal.Season
import com.raduitache.myanimelist.services.anime.AnimeService
import retrofit2.await

class SeasonalAnimePagingSource(
    private val animeService: AnimeService,
    private val year: Int,
    private val season: Season,
    private val sorting: Sorting
) : PagingSource<Int, Anime>() {
    override fun getRefreshKey(state: PagingState<Int, Anime>): Int? = state.anchorPosition?.let {
        state.closestPageToPosition(it)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Anime> {
        Log.w("AnimePagingSource", "Year: $year, season: $season")
        val seasonalAnime = animeService.getSeasonalAnime(
            year = year,
            season = season,
            page = params.key?.let { it * params.loadSize } ?: 0,
            sort = sorting.name.lowercase()
        ).await()
        Log.w("AnimePagingSource", seasonalAnime.values.first().toString())

        return LoadResult.Page(
            data = seasonalAnime.values,
            prevKey = seasonalAnime.paging.previous?.let { params.key?.dec() },
            nextKey = seasonalAnime.paging.next?.let { params.key?.inc() },
        )
    }
}