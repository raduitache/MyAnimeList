package com.raduitache.myanimelist.seasonal.impl

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.raduitache.myanimelist.R
import com.raduitache.myanimelist.navigation.ui.MainNavigationBarItem
import com.raduitache.myanimelist.seasonal.SeasonalGraphRoute
import com.raduitache.myanimelist.seasonal.SeasonalNavGraph
import com.raduitache.myanimelist.seasonal.SeasonalNavRoute
import javax.inject.Inject

class SeasonalNavGraphImpl @Inject constructor(
    graphRoute: SeasonalGraphRoute,
    private val startDestination: SeasonalNavRoute,
    private val navController: NavController,
) : SeasonalNavGraph(graphRoute, startDestination) {
    override val navItemIndex: Int = 1

    @Composable
    override fun NavigationItem(selected: Boolean, rowScope: RowScope) {
        val label = stringResource(id = R.string.seasonal_title)

        MainNavigationBarItem(
            selected = selected,
            rowScope = rowScope,
            icon = rememberVectorPainter(Icons.Outlined.DateRange),
            label = label,
            onClick = { navController.navigate(graphRoute.route) },
        )
    }

    override fun NavGraphBuilder.buildNestedNavGraph() {
        composable(startDestination.route, startDestination.namedNavArgs) {
            startDestination.Content()
        }
    }
}