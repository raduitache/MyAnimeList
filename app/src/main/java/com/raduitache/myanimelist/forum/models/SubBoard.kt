package com.raduitache.myanimelist.forum.models

import com.google.gson.annotations.SerializedName

data class SubBoard (

    @SerializedName("id"    ) var id    : Int?    = null,
    @SerializedName("title" ) var title : String? = null

)