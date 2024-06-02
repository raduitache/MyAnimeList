package com.raduitache.myanimelist.mylist.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.raduitache.myanimelist.R
import com.raduitache.myanimelist.signin.impl.SignInScreen

@Composable
fun MyListScreen(signIn: () -> Unit, viewModel: MyListViewModel = hiltViewModel()) {
    if (viewModel.isSignedIn) {
        // TODO: Show my list
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = R.string.sign_in_label))

                TextButton(onClick = signIn) {
                    Text(text = stringResource(id = R.string.sign_in_button_label))
                }
            }
        }
    }
}