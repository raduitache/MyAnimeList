package com.raduitache.myanimelist.services.mylist

import com.raduitache.myanimelist.responses.Anime
import com.raduitache.myanimelist.responses.Response
import retrofit2.Call
import retrofit2.http.GET

interface MyListService {
    @GET("users/@me/animelist")
    fun getMyList(): Call<Response<Anime>>
}