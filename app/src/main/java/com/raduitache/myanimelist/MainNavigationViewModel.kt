package com.raduitache.myanimelist

import androidx.lifecycle.ViewModel
import com.raduitache.myanimelist.main.NavGraph
import com.raduitache.myanimelist.navigation.MainNavGraph
import com.raduitache.myanimelist.seasonal.SeasonalNavGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainNavigationViewModel @Inject constructor(
    internal val startDestination: SeasonalNavGraph,
    internal val mainNavGraphs: Set<@JvmSuppressWildcards MainNavGraph>,
    internal val navGraphs: Set<@JvmSuppressWildcards NavGraph>
) : ViewModel()