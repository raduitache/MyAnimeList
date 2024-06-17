package com.raduitache.myanimelist.forum.impl

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.raduitache.myanimelist.R
import com.raduitache.myanimelist.forum.ForumGraphRoute
import com.raduitache.myanimelist.forum.ForumNavGraph
import com.raduitache.myanimelist.forum.ForumNavRoute
import com.raduitache.myanimelist.navigation.ui.MainNavigationBarItem
import javax.inject.Inject

class ForumNavGraphImpl @Inject constructor(
    graphRoute: ForumGraphRoute,
    private val startDestination: ForumNavRoute,
) : ForumNavGraph(graphRoute, startDestination) {
    override val navItemIndex: Int = 3

    @Composable
    override fun NavigationItem(selected: Boolean, rowScope: RowScope, navController: NavController) {
        val label = stringResource(id = R.string.Forum_title)

        MainNavigationBarItem(
            selected = selected,
            rowScope = rowScope,
            icon = rememberVectorPainter(Icons.Outlined.MailOutline),
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