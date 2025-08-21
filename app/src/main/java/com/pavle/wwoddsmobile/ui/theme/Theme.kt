package com.pavle.wwoddsmobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary      = Color(0xFF80B3FF),
    onPrimary    = Color(0xFF002544),
    background   = Color.Black,
    surface      = Color.Black,
    onBackground = Color.White,
    onSurface    = Color.White
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}
