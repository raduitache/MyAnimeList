package com.raduitache.myanimelist.signin.impl

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.raduitache.myanimelist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(onBack: () -> Unit,viewModel: SignInViewModel = hiltViewModel()) {
    val activity = LocalContext.current as Activity
    val uiData by viewModel.uiData.collectAsState()

    val completionState = uiData.completionState
    if (completionState == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else if (completionState.isSuccess) {
        LaunchedEffect(Unit) {
            onBack()
        }
    } else {
        BasicAlertDialog(onDismissRequest = onBack) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = stringResource(id = R.string.generic_error_title), style = MaterialTheme.typography.titleMedium)
                Text(text = completionState.exceptionOrNull()?.message ?: "", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    if (uiData.code != null) {
        LaunchedEffect(viewModel) {
            viewModel.exchangeCodesAndLogIn()
        }
    } else {
        LaunchedEffect(viewModel) {
            viewModel.signIn(activity)
        }
    }
}