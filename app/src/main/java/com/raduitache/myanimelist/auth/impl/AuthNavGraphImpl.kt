package com.raduitache.myanimelist.auth.impl

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.raduitache.myanimelist.auth.AuthGraphRoute
import com.raduitache.myanimelist.auth.AuthNavGraph
import com.raduitache.myanimelist.auth.AuthNavRoute
import com.raduitache.myanimelist.navigation.ui.MainNavigationBarItem
import javax.inject.Inject

class AuthNavGraphImpl @Inject constructor(
    graphRoute: AuthGraphRoute,
    private val startDestination: AuthNavRoute,
) : AuthNavGraph(graphRoute, startDestination) {
    override val navItemIndex: Int = 0

    @Composable
    override fun NavigationItem(selected: Boolean, rowScope: RowScope, navController: NavController) {
        val label = "auth"

        MainNavigationBarItem(
            selected = selected,
            rowScope = rowScope,
            icon = rememberVectorPainter(Icons.Outlined.DateRange),
            label = label,
            onClick = { navController.navigate(graphRoute.route) },
        )
    }

    override fun NavGraphBuilder.buildNestedNavGraph(navController: NavController) {
        composable(startDestination.route, startDestination.namedNavArgs) {
            startDestination.Content()
        }
    }
}