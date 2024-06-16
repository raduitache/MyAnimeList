package com.raduitache.myanimelist.auth

import com.raduitache.myanimelist.main.NavGraph

abstract class AuthNavGraph(graphRoute: AuthGraphRoute, startDestination: AuthNavRoute): NavGraph(graphRoute, startDestination)