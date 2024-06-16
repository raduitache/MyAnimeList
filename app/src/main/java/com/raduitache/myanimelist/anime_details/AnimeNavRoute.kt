package com.raduitache.myanimelist.anime_details

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import com.raduitache.myanimelist.navigation.NavRoute

abstract class AnimeNavRoute(route: String, namedNavArgs: List<NamedNavArgument>): NavRoute(route, namedNavArgs) {
    @Composable
    abstract fun Content(onBack: () -> Unit)

}