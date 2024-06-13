package com.raduitache.myanimelist.navigation.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role

@Composable
fun MainNavigationBarItem(
    selected: Boolean,
    rowScope: RowScope,
    icon: Painter,
    label: String,
    onClick: () -> Unit
) {
    rowScope.NavigationBarItem(
        selected = selected,
        onClick = {
            if (selected) return@NavigationBarItem
            onClick.invoke()
        },
        icon = {
            Icon(painter = icon, contentDescription = null)
        },
        modifier = Modifier.clearAndSetSemantics {
            contentDescription = label
            role = Role.Button
        },
        label = {
            Text(text = label, style = MaterialTheme.typography.labelMedium)
        },
        alwaysShowLabel = true
    )
}