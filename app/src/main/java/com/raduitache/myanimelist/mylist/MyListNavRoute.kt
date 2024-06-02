package com.raduitache.myanimelist.mylist

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import com.raduitache.myanimelist.navigation.NavRoute

abstract class MyListNavRoute(route: String, namedNavArgs: List<NamedNavArgument>): NavRoute(route, namedNavArgs) {
    @Composable
    abstract fun Content(signIn: () -> Unit)
}