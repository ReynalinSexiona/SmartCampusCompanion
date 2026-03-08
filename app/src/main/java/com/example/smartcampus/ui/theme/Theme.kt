package com.example.smartcampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = CampusGreen,
    onPrimary = White,
    secondary = CampusYellow,
    onSecondary = DarkGreen,
    background = Color(0xFFF1F8E9), // Light Greenish background
    onBackground = Color.Black,
    surface = White,
    onSurface = Color.Black,
)

private val DarkColors = darkColorScheme(
    primary = CampusLightGreen,
    onPrimary = Color.Black,
    secondary = CampusYellow,
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = White,
    surface = Color(0xFF1E1E1E),
    onSurface = White,
)

@Composable
fun SmartCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
