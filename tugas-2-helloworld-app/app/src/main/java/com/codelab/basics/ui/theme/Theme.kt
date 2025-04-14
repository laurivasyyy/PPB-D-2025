package com.codelab.basics.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dark theme with soft pink accents
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFF48FB1),      // Light Pink
    secondary = Color(0xFFCE93D8),    // Lavender Pink
    tertiary = Color(0xFFFFABAB),     // Soft Salmon
    background = Color(0xFF2A2A2A),   // Dark background
    surface = Color(0xFF3A3A3A),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

// Light theme with soft pink shades
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFFC1E3),      // Baby Pink
    secondary = Color(0xFFFFD6E8),    // Light Rose
    tertiary = Color(0xFFFFE4EC),     // Pale Pink
    background = Color(0xFFFFF5F7),   // Lightest pinkish white
    surface = Color(0xFFFFEBF0),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun BasicsCodelabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
