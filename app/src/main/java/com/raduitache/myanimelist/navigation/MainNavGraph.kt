package com.raduitache.myanimelist.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.raduitache.myanimelist.main.NavGraph

abstract class MainNavGraph(graphRoute: NavRoute, startDestination: NavRoute): NavGraph(graphRoute, startDestination) {
    abstract val navItemIndex: Int
    @Composable
    abstract fun NavigationItem(selected: Boolean, rowScope: RowScope, navController: NavController)
}
