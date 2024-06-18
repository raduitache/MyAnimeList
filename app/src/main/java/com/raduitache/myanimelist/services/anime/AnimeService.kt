package com.raduitache.myanimelist.services.anime

import com.raduitache.myanimelist.responses.Anime
import com.raduitache.myanimelist.responses.Response
import com.raduitache.myanimelist.responses.Sorting
import com.raduitache.myanimelist.seasonal.Season
import com.raduitache.myanimelist.services.ServicesConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeService {
    @GET("anime/season/{year}/{season}")
    fun getSeasonalAnime(
        @Path("year") year: Int,
        @Path("season") season: Season,
        @Query("offset") page: Int = 0,
        @Query("limit") limit: Int = ServicesConstants.REQUEST_ITEM_COUNT,
        @Query("sort") sort: String
    ): Call<Response<Response.DataItem<Anime>>>

    @GET("anime/{anime_id}")
    fun getSeasonalAnime(
        @Path("anime_id") animeId: String,
        @Query("fields") fields: String,
    ): Call<Anime>
}