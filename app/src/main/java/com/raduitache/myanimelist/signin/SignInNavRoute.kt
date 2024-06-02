package com.raduitache.myanimelist.signin

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import com.raduitache.myanimelist.navigation.NavRoute

abstract class SignInNavRoute(route: String, namedNavArgs: List<NamedNavArgument>): NavRoute(route, namedNavArgs) {
    @Composable
    abstract fun Content(backStackEntry: NavBackStackEntry, onBack: () -> Unit)
}