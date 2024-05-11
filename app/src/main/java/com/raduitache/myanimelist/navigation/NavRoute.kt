package com.raduitache.myanimelist.navigation

import androidx.navigation.NamedNavArgument

/** Route for a screen in a [NavGraph]. */
abstract class NavRoute(val route: String, val namedNavArgs: List<NamedNavArgument>)