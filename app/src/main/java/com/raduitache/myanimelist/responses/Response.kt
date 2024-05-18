package com.raduitache.myanimelist.responses

data class Response<T>(
    private val data: List<DataItem<T>>,
    val paging: Paging
) {
    data class DataItem<K>(
        val node: K
    )

    val values: List<T>
        get() = data.map { it.node }
}