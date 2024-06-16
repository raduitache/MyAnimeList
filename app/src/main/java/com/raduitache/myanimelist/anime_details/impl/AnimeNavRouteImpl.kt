package com.raduitache.myanimelist.anime_details.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.raduitache.myanimelist.anime_details.AnimeNavRoute
import javax.inject.Inject

class AnimeNavRouteImpl @Inject constructor() : AnimeNavRoute("anime/{animeId}/anime-screen",
    listOf(
        navArgument("animeId") {
            type = NavType.StringType
            nullable = false
        }
    )
) {
    @Composable
    override fun Content(onBack: () -> Unit) {
        AnimeDetailsScreen(onBack = onBack)
    }

}