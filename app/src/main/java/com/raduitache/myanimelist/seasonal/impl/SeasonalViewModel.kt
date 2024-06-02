package com.raduitache.myanimelist.seasonal.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.raduitache.myanimelist.responses.Anime
import com.raduitache.myanimelist.responses.Sorting
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import java.time.Year
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class SeasonalViewModel @Inject constructor(private val animeServiceFlow: Flow<@JvmSuppressWildcards AnimeService>) : ViewModel() {
    private val _screenState = MutableStateFlow(SeasonalScreenState())

    private val _pager =
        combine(
            animeServiceFlow,
            _screenState
        ) { animeService, screenState ->
            Pager(
                config = PagingConfig(pageSize = ServicesConstants.REQUEST_ITEM_COUNT),
                initialKey = 0,
            ) {
                SeasonalAnimePagingSource(
                    animeService = animeService,
                    year = screenState.selectedYear,
                    season = screenState.selectedSeason,
                    sorting = screenState.sorting
                )
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pager = _pager.flatMapLatest { it.flow }
    val screenState = _screenState.asStateFlow()

    fun changeSeasonTo(newSeason: Season, newYear: Int) {
        _screenState.update {
            it.copy(selectedSeason = newSeason, selectedYear = newYear)
        }
    }

    fun generateLastSeasons(): List<Pair<Season, Int>> {
        val currentSeason = _screenState.value.selectedSeason
        val currentYear = _screenState.value.selectedYear
        val hey = Season.generateLastSeasons(5, currentSeason, currentYear).toMutableList()
        if (currentSeason != Season.current() || currentYear != Season.currentYear()) {
            hey.add(0, Pair(Season.current(), Season.currentYear()))
        }
        return hey
    }

    fun changeSorting() {
        val toTypedArray = Sorting.entries.toTypedArray()
        val currentIndex = toTypedArray.indexOf(_screenState.value.sorting)
        val newSortOrder = try {
            toTypedArray[currentIndex + 1]
        } catch (_: Exception) {
            Sorting.ANIME_SCORE
        }
        _screenState.update {
            it.copy(
                sorting = newSortOrder
            )
        }
    }
}

data class SeasonalScreenState (
    val selectedYear: Int = Year.now(TimeZone.getDefault().toZoneId()).value,
    val selectedSeason: Season = Season.current(),
    val sorting: Sorting = Sorting.ANIME_SCORE
)