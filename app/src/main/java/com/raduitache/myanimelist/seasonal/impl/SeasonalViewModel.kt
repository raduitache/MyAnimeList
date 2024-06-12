package com.raduitache.myanimelist.seasonal.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.raduitache.myanimelist.MainApplication
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Year
import java.util.TimeZone
import javax.inject.Inject

private const val PREF_SELECTED_SEASON = "SELECTED_SEASON"
private const val PREF_SELECTED_YEAR = "SELECTED_YEAR"
private const val PREF_SELECTED_SORT = "SELECTED_SORT"

@HiltViewModel
class SeasonalViewModel @Inject constructor(
    private val animeServiceFlow: Flow<@JvmSuppressWildcards AnimeService>,
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {
    private val _pager =
        combine(
            animeServiceFlow,
            dataStore.data,
        ) { animeService, prefs ->
            Pager(
                config = PagingConfig(pageSize = ServicesConstants.REQUEST_ITEM_COUNT),
                initialKey = 0,
            ) {
                val selectedSeason = try {
                    Season.valueOf(prefs[stringPreferencesKey(PREF_SELECTED_SEASON)]!!)
                } catch (_: Exception) {
                    Season.current()
                }

                val sorting = try {
                    Sorting.valueOf(prefs[stringPreferencesKey(PREF_SELECTED_SORT)]!!)
                } catch (_: Exception) {
                    Sorting.ANIME_SCORE
                }

                SeasonalAnimePagingSource(
                    animeService = animeService,
                    year = prefs[intPreferencesKey(PREF_SELECTED_YEAR)]
                        ?: Year.now(TimeZone.getDefault().toZoneId()).value,
                    season = selectedSeason,
                    sorting = sorting
                )
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pager = _pager.flatMapLatest { it.flow }
    val screenState = dataStore.data.map { prefs: Preferences ->
        val selectedSeason = try {
            Season.valueOf(prefs[stringPreferencesKey(PREF_SELECTED_SEASON)]!!)
        } catch (_: Exception) {
            Season.current()
        }

        val sorting = try {
            Sorting.valueOf(prefs[stringPreferencesKey(PREF_SELECTED_SORT)]!!)
        } catch (_: Exception) {
            Sorting.ANIME_SCORE
        }

        val selectedYear = prefs[intPreferencesKey(PREF_SELECTED_YEAR)]
            ?: Year.now(TimeZone.getDefault().toZoneId()).value

        val hey = Season.generateLastSeasons(5, selectedSeason, selectedYear).toMutableList()
        if (selectedSeason != Season.current() || selectedYear != Season.currentYear()) {
            hey.add(0, Pair(Season.current(), Season.currentYear()))
        }

        SeasonalScreenState(
            selectedYear = selectedYear,
            sorting = sorting,
            selectedSeason = selectedSeason,
            lastSeasons = hey
        )
    }

    fun changeSeasonTo(newSeason: Season, newYear: Int) {
        viewModelScope.launch {
            dataStore.edit {
                it[stringPreferencesKey(PREF_SELECTED_SEASON)] = newSeason.name
                it[intPreferencesKey(PREF_SELECTED_YEAR)] = newYear
            }
        }
    }

    fun changeSorting() {
        viewModelScope.launch {
            val toTypedArray = Sorting.entries.toTypedArray()
            val currentIndex = toTypedArray.indexOf(screenState.first().sorting)
            val newSortOrder = try {
                toTypedArray[currentIndex + 1]
            } catch (_: Exception) {
                Sorting.ANIME_SCORE
            }
            dataStore.edit {
                it[stringPreferencesKey(PREF_SELECTED_SORT)] = newSortOrder.name
            }
        }
    }
}

data class SeasonalScreenState(
    val selectedYear: Int = Year.now(TimeZone.getDefault().toZoneId()).value,
    val selectedSeason: Season = Season.current(),
    val sorting: Sorting = Sorting.ANIME_SCORE,
    val lastSeasons: List<Pair<Season, Int>> = emptyList(),
)