package com.raduitache.myanimelist.responses

import com.google.gson.annotations.SerializedName

data class Anime(
    val id: Int,
    val title: String,
    @SerializedName("main_picture")
    val mainPicture: MainPicture,
    val mean: String?,
    val rank: Int?,
    @SerializedName("start_date")
    val startDate: String?,
    @SerializedName("end_date")
    val endDate: String?,
    val popularity: Int,
    val source: String,
    val genres: List<Genre>,
    val synopsis: String?,
    @SerializedName("num_episodes")
    val num_episodes: String,
    val status: String,
    @SerializedName("my_list_status")
    val myList: MyListStatus?,
    val pictures: List<MainPicture>,
    val statistics: Statistics
)

data class MainPicture (
    val large: String?,
    val medium: String?
)

data class MyListStatus (
    val status: String,
    val score: Int?,
)

data class Statistics (
    val num_list_users: Int,
    @SerializedName("status")
    val from: From
)

data class From(
    val watching: String,
    val completed: String,
    val on_hold: String,
    val dropped: String,
    val plan_to_watch: String,
)