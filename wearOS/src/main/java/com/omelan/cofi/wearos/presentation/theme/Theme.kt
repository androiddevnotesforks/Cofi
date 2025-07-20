package com.omelan.cofi.wearos.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.dynamicColorScheme

@Composable
fun CofiTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    dynamicColorScheme(context)?.let {
        MaterialTheme(
            colorScheme = it,
            content = content
        )
    } ?: MaterialTheme(
        colorScheme = wearColorPalette,
        content = content
    )
}
