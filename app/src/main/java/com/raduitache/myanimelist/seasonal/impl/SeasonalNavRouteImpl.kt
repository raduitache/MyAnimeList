package com.raduitache.myanimelist.seasonal.impl

import androidx.compose.runtime.Composable
import com.raduitache.myanimelist.seasonal.SeasonalNavRoute
import javax.inject.Inject

class SeasonalNavRouteImpl @Inject constructor(): SeasonalNavRoute("seasonal/seasonal-screen", emptyList()){
    @Composable
    override fun Content(goToDetails: (String) -> Unit) {
        SeasonalScreen(goToDetails)
    }
}