package com.raduitache.myanimelist.responses

import com.google.gson.annotations.SerializedName

data class Anime(
    val id: Int,
    val title: String,
    @SerializedName("main_picture")
    val mainPicture: MainPicture,
    val mean: Float?,
    val rank: Int?,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    val popularity: Int,
    val source: String,
    val genres: List<Genre>
)

data class MainPicture (
    val large: String?,
    val medium: String?
)