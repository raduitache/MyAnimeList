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
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigation(viewModel: MainNavigationViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            if (!viewModel.mainNavGraphs.map { it.graphRoute.route }
                    .contains(currentBackStackEntry?.destination?.route)) return@Scaffold

            NavigationBar {
                for (navGraph in viewModel.mainNavGraphs) {
                    navGraph.NavigationBarItem(
                        selected = currentBackStackEntry?.destination?.route == navGraph.graphRoute.route
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "dummy",
            modifier = Modifier.padding(it)
        ) {
            for (navGraph in viewModel.navGraphs) {
                navGraph.addToNavGraph(this)
            }
        }
    }
}