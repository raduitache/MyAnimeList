package com.raduitache.myanimelist.anime_details.impl

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.raduitache.myanimelist.anime_details.AnimeGraphRoute
import com.raduitache.myanimelist.anime_details.AnimeNavGraph
import com.raduitache.myanimelist.anime_details.AnimeNavRoute
import javax.inject.Inject

class AnimeNavGraphImpl @Inject constructor(
    graphRoute: AnimeGraphRoute,
    private val startDestination: AnimeNavRoute
) : AnimeNavGraph(graphRoute = graphRoute, startDestination = startDestination) {
    override fun NavGraphBuilder.buildNestedNavGraph(navController: NavController) {
        composable(startDestination.route, startDestination.namedNavArgs) {
            startDestination.Content {
                navController.navigateUp()
            }
        }
    }
}