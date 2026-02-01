package com.example.smartcampus.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = CampusGreen,
    onPrimary = White,
    secondary = CampusYellow,
    onSecondary = DarkGreen,
    background = BackgroundGreen,
    onBackground = White,
    surface = CampusGreen,
    onSurface = White,
)

@Composable
fun SmartCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography(),
        content = content
    )
}
