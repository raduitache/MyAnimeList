package com.raduitache.myanimelist.services.forum

import com.raduitache.myanimelist.forum.models.Data
import com.raduitache.myanimelist.forum.models.ForumResponse
import com.raduitache.myanimelist.responses.Anime
import com.raduitache.myanimelist.responses.Response
import com.raduitache.myanimelist.seasonal.Season
import com.raduitache.myanimelist.services.ServicesConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ForumService {
    @GET("forum/boards")
    fun getBoards(): Call<ForumResponse>

    @GET("forum/topics")
    fun getBoardData(
        @Query("board_id") boardId: Int,
        @Query("offset") page: Int = 0,
        @Query("limit") limit: Int = ServicesConstants.REQUEST_ITEM_COUNT,
    ): Call<Response<Data>>

}

