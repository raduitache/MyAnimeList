package com.raduitache.myanimelist.forum.models

import com.google.gson.annotations.SerializedName

data class Categories (
    @SerializedName("title"  ) var title  : String?           = null,
    @SerializedName("boards" ) var boards : List<Boards> = arrayListOf()

)