package com.raduitache.myanimelist.signin.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.raduitache.myanimelist.signin.SignInNavRoute
import javax.inject.Inject

class SignInNavRouteImpl @Inject constructor() :
    SignInNavRoute("signin/screen", namedNavArgs = listOf(
        navArgument("code") {
            type = NavType.StringType
        },
        navArgument("state") {
            type = NavType.StringType
        }
    )
    ) {
    @Composable
    override fun Content(backStackEntry: NavBackStackEntry, onBack: () -> Unit) {
        val viewModel = hiltViewModel<SignInViewModel>()
        LaunchedEffect(viewModel) {
            viewModel.savedStateHandle["code"] = backStackEntry.arguments?.getString("code")
            viewModel.savedStateHandle["state"] = backStackEntry.arguments?.getString("state")
        }

        SignInScreen(onBack = onBack, viewModel = viewModel)
    }
}