package com.raduitache.myanimelist.seasonal.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.raduitache.myanimelist.seasonal.Season
import com.raduitache.myanimelist.seasonal.impl.paging.SeasonalAnimePagingSource
import com.raduitache.myanimelist.services.ServicesConstants
import com.raduitache.myanimelist.services.anime.AnimeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Year
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class SeasonalViewModel @Inject constructor(animeServiceFlow: Flow<@JvmSuppressWildcards AnimeService>) : ViewModel() {
    private val _pager = animeServiceFlow.map { animeService ->
        Pager(
            config = PagingConfig(pageSize = ServicesConstants.REQUEST_ITEM_COUNT),
            initialKey = 0,
        ) {
            SeasonalAnimePagingSource(animeService = animeService,
                year = Year.now(TimeZone.getDefault().toZoneId()).value,
                season = Season.current())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pager = _pager.flatMapLatest { it.flow }
}