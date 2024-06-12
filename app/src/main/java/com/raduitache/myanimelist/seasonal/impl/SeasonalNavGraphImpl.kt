package com.raduitache.myanimelist.seasonal.impl

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.raduitache.myanimelist.R
import com.raduitache.myanimelist.anime_details.AnimeDetailsScreen
import com.raduitache.myanimelist.anime_details.AnimeDetailsViewModel
import com.raduitache.myanimelist.navigation.ui.MainNavigationBarItem
import com.raduitache.myanimelist.seasonal.SeasonalGraphRoute
import com.raduitache.myanimelist.seasonal.SeasonalNavGraph
import com.raduitache.myanimelist.seasonal.SeasonalNavRoute
import com.raduitache.myanimelist.settings.SettingsScreen
import javax.inject.Inject

class SeasonalNavGraphImpl @Inject constructor(
    graphRoute: SeasonalGraphRoute,
    private val startDestination: SeasonalNavRoute,
) : SeasonalNavGraph(graphRoute, startDestination) {
    override val navItemIndex: Int = 1

    @Composable
    override fun NavigationItem(selected: Boolean, rowScope: RowScope, navController: NavController) {
        val label = stringResource(id = R.string.seasonal_title)

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
            startDestination.Content {
                navController.navigate("details/$it")
            }
        }
        composable(
            "details/{details}",
            arguments = listOf(navArgument("details") {type = NavType.StringType})
        ) { backStackEntry ->
            val animeDetailsViewModel = hiltViewModel<AnimeDetailsViewModel>()
            backStackEntry.arguments?.getString("details")?.let { animeDetailsViewModel.selectAnime(it) }
            AnimeDetailsScreen(animeDetailsViewModel) {
                navController.popBackStack()
            }
        }
        composable("settings") {
            SettingsScreen()
        }

    }
}