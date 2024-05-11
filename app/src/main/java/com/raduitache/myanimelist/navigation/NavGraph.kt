package com.raduitache.myanimelist.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import androidx.navigation.NavHost
import com.raduitache.myanimelist.navigation.NavRoute

/** A nested navigation graph to be used in a [NavHost]. It lives next to [NavGraph]s corresponding to different features. */
abstract class NavGraph(val graphRoute: NavRoute, private val startDestination: NavRoute) {
    fun addToNavGraph(builder: NavGraphBuilder) {
        builder.navigation(route = graphRoute.route, startDestination = startDestination.route) {
            buildNestedNavGraph()
        }
    }

    abstract fun NavGraphBuilder.buildNestedNavGraph()
}