package com.raduitache.myanimelist.anime_details.impl

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.raduitache.myanimelist.anime_details.AnimeGraphRoute
import javax.inject.Inject

class AnimeGraphRouteImpl @Inject constructor() :
    AnimeGraphRoute(route = "anime/{animeId}", namedNavArgs =
    listOf(
        navArgument("animeId") {
            type = NavType.StringType
            nullable = false
        }
    )) {
    override fun navigateToAnimeDetails(animeId: String): String = "anime/$animeId"
}