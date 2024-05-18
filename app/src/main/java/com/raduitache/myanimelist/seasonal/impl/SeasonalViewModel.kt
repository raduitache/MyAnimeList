package com.raduitache.myanimelist.seasonal.impl

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.raduitache.myanimelist.seasonal.Season
import com.raduitache.myanimelist.seasonal.impl.paging.SeasonalAnimePagingSource
import com.raduitache.myanimelist.services.ServicesConstants
import com.raduitache.myanimelist.services.anime.AnimeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import java.time.Year
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class SeasonalViewModel @Inject constructor(private val animeService: AnimeService) : ViewModel() {
    private val _pager = MutableStateFlow(
        Pager(
            config = PagingConfig(pageSize = ServicesConstants.REQUEST_ITEM_COUNT),
            initialKey = 0,
        ) {
            SeasonalAnimePagingSource(animeService = animeService,
                year = Year.now(TimeZone.getDefault().toZoneId()).value,
                season = Season.current())
        }
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val pager = _pager.flatMapLatest { it.flow }
}