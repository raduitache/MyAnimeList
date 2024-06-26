package com.raduitache.myanimelist.mylist.impl

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.raduitache.myanimelist.R
import com.raduitache.myanimelist.anime_details.AnimeGraphRoute
import com.raduitache.myanimelist.auth.AuthNavRoute
import com.raduitache.myanimelist.mylist.MyListGraphRoute
import com.raduitache.myanimelist.mylist.MyListNavGraph
import com.raduitache.myanimelist.mylist.MyListNavRoute
import com.raduitache.myanimelist.navigation.ui.MainNavigationBarItem
import com.raduitache.myanimelist.signin.SignInGraphRoute
import javax.inject.Inject

class MyListNavGraphImpl @Inject constructor(
    graphRoute: MyListGraphRoute,
    private val startDestination: MyListNavRoute,
    private val authNavRoute: AuthNavRoute,
    private val signInGraphRoute: SignInGraphRoute,
    private val animeDetailsGraphRoute: AnimeGraphRoute,
) : MyListNavGraph(graphRoute, startDestination) {
    override val navItemIndex: Int = 2

    @Composable
    override fun NavigationItem(
        selected: Boolean,
        rowScope: RowScope,
        navController: NavController
    ) {
        val label = stringResource(R.string.my_list_title)

        MainNavigationBarItem(
            selected = selected,
            rowScope = rowScope,
            icon = rememberVectorPainter(Icons.AutoMirrored.Outlined.List),
            label = label,
            onClick = { navController.navigate(graphRoute.route) },
        )
    }

    override fun NavGraphBuilder.buildNestedNavGraph(navController: NavController) {
        composable(startDestination.route, startDestination.namedNavArgs) {
            startDestination.Content(onAnimeClick = { animeId ->
                navController.navigate(
                    animeDetailsGraphRoute.navigateToAnimeDetails(animeId)
                )
            }) {
                authNavRoute.Content {
                    navController.navigate(signInGraphRoute.route)
                }
            }
        }
    }
}