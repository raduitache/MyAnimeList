package com.raduitache.myanimelist.mylist.impl

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.raduitache.myanimelist.mylist.MyListNavRoute
import javax.inject.Inject

class MyListNavRouteImpl @Inject constructor() :
    MyListNavRoute(route = "my-list/my-list-screen", namedNavArgs = listOf(
        navArgument("code") {
            nullable = true
            type = NavType.StringType
        }
    )) {
    @Composable
    override fun Content(authScreen: @Composable () -> Unit) {
        MyListScreen(authScreen)
    }
}