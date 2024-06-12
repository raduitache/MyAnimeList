package com.raduitache.myanimelist.responses

import com.google.gson.annotations.SerializedName


data class ListStatus(
    val status: String? = null,
    val score: Int = 0,
    @SerializedName("num_episodes_watched") val numEpisodesWatched: Int = 0,
    @SerializedName("start_date") val startDate: String = "",
    @SerializedName("finish_date") val finishDate: String = "",
    @SerializedName("updated_at") val updatedAt: String = "",
)
