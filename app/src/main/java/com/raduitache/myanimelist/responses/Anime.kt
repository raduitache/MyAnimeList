package com.raduitache.myanimelist.responses

import com.google.gson.annotations.SerializedName

data class Anime(
    val id: Int,
    val title: String = "",
    @SerializedName("main_picture") val mainPicture: CoverImage? = null,
    @SerializedName("num_episodes") val numEpisodes: Int = 0,
    @SerializedName("start_date") val startDate: String = "",
    @SerializedName("end_date") val endDate: String = "",
    @SerializedName("media_type") val mediaType: String = "",
    @SerializedName("start_season") val startSeason: StartSeason? = null,
)
