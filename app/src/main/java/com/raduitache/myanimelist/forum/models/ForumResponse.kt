package com.raduitache.myanimelist.forum.models

import com.google.gson.annotations.SerializedName

data class ForumResponse (
    @SerializedName("categories" ) var categories : List<Categories> = arrayListOf()
)