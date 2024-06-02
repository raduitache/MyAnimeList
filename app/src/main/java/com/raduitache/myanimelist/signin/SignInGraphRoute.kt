package com.raduitache.myanimelist.signin

import androidx.navigation.NamedNavArgument
import com.raduitache.myanimelist.navigation.NavRoute

abstract class SignInGraphRoute(route: String, namedNavArgs: List<NamedNavArgument>): NavRoute(route, namedNavArgs)