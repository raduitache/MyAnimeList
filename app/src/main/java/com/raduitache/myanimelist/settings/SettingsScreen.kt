package com.raduitache.myanimelist.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raduitache.myanimelist.seasonal.impl.SeasonalViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val toggleDarkTheme = viewModel.defaultTheme.collectAsState(false)

    Column {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Dark theme")
            Checkbox(checked = toggleDarkTheme.value ?: false, onCheckedChange = {
                viewModel.toggleBooleanPreference()
            })

        }
    }

}

const val PREF_SELECTED_DARK_MODE = "PREF_SELECTED_DARK_MODE"

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
): ViewModel() {
    val defaultTheme = dataStore.data.map {
        try {
            it[booleanPreferencesKey(PREF_SELECTED_DARK_MODE)]
        } catch (_: Exception) {
            false
        }
    }

    fun toggleBooleanPreference() {
        viewModelScope.launch {
            dataStore.edit {
                it[booleanPreferencesKey(PREF_SELECTED_DARK_MODE)] = (defaultTheme.firstOrNull()?:false).not()
            }
        }
    }

}