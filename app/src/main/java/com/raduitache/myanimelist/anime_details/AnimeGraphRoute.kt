package com.raduitache.myanimelist.anime_details

import androidx.navigation.NamedNavArgument
import com.raduitache.myanimelist.navigation.NavRoute

abstract class AnimeGraphRoute(route: String, namedNavArgs: List<NamedNavArgument>): NavRoute(route, namedNavArgs){
    abstract fun navigateToAnimeDetails(animeId: String): String
}