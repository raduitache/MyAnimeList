package com.raduitache.myanimelist.auth

import com.raduitache.myanimelist.navigation.MainNavGraph

abstract class AuthNavGraph(graphRoute: AuthGraphRoute, startDestination: AuthNavRoute): MainNavGraph(graphRoute, startDestination)