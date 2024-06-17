package com.raduitache.myanimelist.forum.models

import com.google.gson.annotations.SerializedName

data class Boards (
    @SerializedName("id"          ) var id          : Int?              = null,
    @SerializedName("title"       ) var title       : String?           = null,
    @SerializedName("description" ) var description : String?           = null,
    @SerializedName("subboards"   ) var subboards   : List<SubBoard> = arrayListOf()
)