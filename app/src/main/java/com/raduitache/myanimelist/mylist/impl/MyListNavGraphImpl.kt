package com.raduitache.myanimelist.mylist.impl

import android.util.Log
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.raduitache.myanimelist.R
import com.raduitache.myanimelist.mylist.MyListGraphRoute
import com.raduitache.myanimelist.mylist.MyListNavGraph
import com.raduitache.myanimelist.mylist.MyListNavRoute
import com.raduitache.myanimelist.navigation.ui.MainNavigationBarItem
import com.raduitache.myanimelist.signin.SignInGraphRoute
import javax.inject.Inject

class MyListNavGraphImpl @Inject constructor(
    graphRoute: MyListGraphRoute,
    private val startDestination: MyListNavRoute,
    private val signInGraphRoute: SignInGraphRoute
): MyListNavGraph(graphRoute, startDestination) {
    override val navItemIndex: Int = 2

    @Composable
    override fun NavigationItem(selected: Boolean, rowScope: RowScope, navController: NavController) {
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
            startDestination.Content(signIn = { navController.navigate(signInGraphRoute.route)})
        }
    }
}