package com.raduitache.myanimelist

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(viewModel: MainNavigationViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        topBar = {
            if (currentBackStackEntry?.destination?.route?.contains("settings") == true) {
                TopAppBar(title = {
                                  Text(text = "Settings")
                }, navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                })
            } else {
                TopAppBar(title = {

                }, actions = {
                    IconButton(onClick = {
                        navController.navigate("settings")
                    }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                    }
                })
            }
        },
        bottomBar = {
            if (!viewModel.mainNavGraphs.map { it.graphRoute.route }.any { currentBackStackEntry?.destination?.route?.startsWith(it) == true }) return@Scaffold
            if (currentBackStackEntry?.destination?.route?.contains("settings") == true) { return@Scaffold }
            NavigationBar {
                for (navGraph in viewModel.mainNavGraphs.sortedBy { it.navItemIndex }) {
                    navGraph.NavigationItem(
                        selected = currentBackStackEntry?.destination?.route?.startsWith(navGraph.graphRoute.route) == true,
                        rowScope = this,
                        navController = navController,
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
                navGraph.addToNavGraph(this, navController)
            }
        }
    }
}