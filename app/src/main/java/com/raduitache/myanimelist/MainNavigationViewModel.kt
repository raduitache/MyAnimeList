package com.raduitache.myanimelist

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.raduitache.myanimelist.auth.AuthNavGraph
import com.raduitache.myanimelist.main.NavGraph
import com.raduitache.myanimelist.navigation.MainNavGraph
import com.raduitache.myanimelist.seasonal.SeasonalNavGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainNavigationViewModel @Inject constructor(
    internal val navController: NavHostController,
    internal val startDestination: AuthNavGraph,
    internal val mainNavGraphs: Set<@JvmSuppressWildcards MainNavGraph>,
    internal val navGraphs: Set<@JvmSuppressWildcards NavGraph>
) : ViewModel()