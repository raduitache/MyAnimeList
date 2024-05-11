package com.raduitache.myanimelist.navigation

import androidx.compose.runtime.Composable
import com.raduitache.myanimelist.main.NavGraph

abstract class MainNavGraph(graphRoute: NavRoute, startDestination: NavRoute): NavGraph(graphRoute, startDestination) {
    @Composable
    abstract fun NavigationBarItem(selected: Boolean)
}
