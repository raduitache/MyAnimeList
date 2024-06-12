package com.raduitache.myanimelist.mylist.impl

import androidx.paging.PagingData
import com.raduitache.myanimelist.responses.Anime
import com.raduitache.myanimelist.responses.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MyListUiData(
    val isSignedIn: Boolean = false,
    val pagingDataFlow: Flow<PagingData<Response.DataItem<Anime>>> = emptyFlow()
)
