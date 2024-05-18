package com.raduitache.myanimelist

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MainNavigation(viewModel: MainNavigationViewModel = hiltViewModel()) {
    val navController = viewModel.navController
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            if (!viewModel.mainNavGraphs.map { it.graphRoute.route }.any { currentBackStackEntry?.destination?.route?.startsWith(it) == true }) return@Scaffold

            NavigationBar {
                for (navGraph in viewModel.mainNavGraphs.sortedBy { it.navItemIndex }) {
                    navGraph.NavigationItem(
                        selected = currentBackStackEntry?.destination?.route?.startsWith(navGraph.graphRoute.route) == true,
                        rowScope = this
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = viewModel.startDestination.graphRoute.route,
            modifier = Modifier.padding(it)
        ) {
            for (navGraph in viewModel.navGraphs) {
                navGraph.addToNavGraph(this)
            }
        }
    }
}