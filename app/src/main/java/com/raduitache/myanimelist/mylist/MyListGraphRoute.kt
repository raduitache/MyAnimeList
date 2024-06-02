package com.raduitache.myanimelist.mylist

import androidx.navigation.NamedNavArgument
import com.raduitache.myanimelist.navigation.NavRoute

abstract class MyListGraphRoute(route: String, namedNavArgs: List<NamedNavArgument>): NavRoute(route, namedNavArgs)