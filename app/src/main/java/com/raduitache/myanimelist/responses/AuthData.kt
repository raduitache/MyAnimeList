package com.raduitache.myanimelist.responses

import com.google.gson.annotations.SerializedName

data class AuthData(
    @SerializedName("tokenType") val tokenType: String = "",
    @SerializedName("accessToken") val accessToken: String = "",
    @SerializedName("refreshToken") val refreshToken: String = "",
    )
