package com.raduitache.myanimelist.forum

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import com.raduitache.myanimelist.navigation.NavRoute

abstract class ForumNavRoute(route: String, namedNavArgs: List<NamedNavArgument>): NavRoute(route, namedNavArgs){
    @Composable
    abstract fun Content()
}