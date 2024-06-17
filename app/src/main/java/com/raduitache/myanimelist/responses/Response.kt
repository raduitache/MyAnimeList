package com.raduitache.myanimelist.responses

import com.google.gson.annotations.SerializedName

data class Response<T>(
    val data: List<T>,
    val paging: Paging
) {
    data class DataItem<K>(
        val node: K,
        @SerializedName("list_status") val listStatus: ListStatus? = null
    )
}