package com.raduitache.myanimelist

import androidx.lifecycle.ViewModel
import com.raduitache.myanimelist.main.NavGraph
import com.raduitache.myanimelist.navigation.MainNavGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainNavigationViewModel @Inject constructor(
    internal val mainNavGraphs: Set<MainNavGraph>,
    internal val navGraphs: Set<NavGraph>
): ViewModel()