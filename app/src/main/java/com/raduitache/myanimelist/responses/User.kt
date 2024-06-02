package com.raduitache.myanimelist.responses

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
)
