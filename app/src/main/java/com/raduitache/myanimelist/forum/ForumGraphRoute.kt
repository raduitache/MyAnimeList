package com.raduitache.myanimelist.forum

import androidx.navigation.NamedNavArgument
import com.raduitache.myanimelist.navigation.NavRoute

abstract class ForumGraphRoute(route: String, namedNavArgs: List<NamedNavArgument>): NavRoute(route, namedNavArgs)