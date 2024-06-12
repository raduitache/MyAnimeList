package com.raduitache.myanimelist.services.user

import com.raduitache.myanimelist.responses.User
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("users/@me")
    fun getUser(): Call<User>
}