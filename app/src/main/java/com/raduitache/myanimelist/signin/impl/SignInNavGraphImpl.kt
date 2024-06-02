package com.raduitache.myanimelist.signin.impl

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.raduitache.myanimelist.signin.SignInGraphRoute
import com.raduitache.myanimelist.signin.SignInNavGraph
import com.raduitache.myanimelist.signin.SignInNavRoute
import javax.inject.Inject

class SignInNavGraphImpl @Inject constructor(
    graphRoute: SignInGraphRoute,
    private val startDestination: SignInNavRoute
) : SignInNavGraph(graphRoute, startDestination) {
    override fun NavGraphBuilder.buildNestedNavGraph(navController: NavController) {
        composable(
            route = startDestination.route,
            arguments = startDestination.namedNavArgs,
            deepLinks = listOf(
                navDeepLink { uriPattern = "app://myanimelist-69e80.firebaseapp.com?code={code}&state={state}" })
        ) {
            startDestination.Content(backStackEntry = it, onBack = { navController.navigateUp()})
        }
    }
}