package com.raduitache.myanimelist.responses

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
) {
    fun toAuthData() = AuthData(tokenType, accessToken, refreshToken)
}
