package com.raduitache.myanimelist.seasonal

import androidx.navigation.NamedNavArgument
import com.raduitache.myanimelist.navigation.NavRoute

abstract class SeasonalGraphRoute(route: String, namedNavArgs: List<NamedNavArgument>): NavRoute(route, namedNavArgs)