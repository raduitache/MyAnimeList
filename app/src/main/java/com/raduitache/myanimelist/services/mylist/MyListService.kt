package com.raduitache.myanimelist.services.mylist

import com.raduitache.myanimelist.responses.Anime
import com.raduitache.myanimelist.responses.Response
import com.raduitache.myanimelist.services.ServicesConstants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface MyListService {
    @GET("users/@me/animelist?fields=num_episodes,main_picture,start_date,end_date,list_status,media_type,start_season&sort=list_updated_at")
    fun getMyList(
        @Query("offset") page: Int = 0,
        @Query("limit") limit: Int = ServicesConstants.REQUEST_ITEM_COUNT,
    ): Call<Response<Anime>>

    @PATCH("anime/{anime_id}/my_list_status")
    @FormUrlEncoded
    fun updateAnimeProgress(
        @Path("anime_id") animeId: Int,
        @Field("num_watched_episodes") watchedEpisodes: Int,
    ): Call<Unit>

    @PATCH("anime/{anime_id}/my_list_status")
    @FormUrlEncoded
    fun updateAnimeStatus(
        @Path("anime_id") animeId: Int,
        @Field("status") status: String,
    ): Call<Unit>
}