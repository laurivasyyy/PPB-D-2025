package com.example.sayurbox.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SayurboxLightColorScheme = lightColorScheme(
    primary = SayurboxGreen,
    onPrimary = Color.White,
    primaryContainer = SayurboxLightGreen,
    onPrimaryContainer = SayurboxDarkGreen,

    secondary = SayurboxLightGreen,
    onSecondary = SayurboxDarkGreen,
    secondaryContainer = SayurboxLightGreen.copy(alpha = 0.3f),
    onSecondaryContainer = SayurboxDarkGreen,

    tertiary = SayurboxDarkGreen,
    onTertiary = Color.White,
    tertiaryContainer = SayurboxGreen.copy(alpha = 0.3f),
    onTertiaryContainer = SayurboxDarkGreen,

    error = SayurboxError,
    onError = Color.White,
    errorContainer = SayurboxError.copy(alpha = 0.1f),
    onErrorContainer = SayurboxError,

    background = SayurboxBackground,
    onBackground = SayurboxTextPrimary,
    surface = SayurboxCardBackground,
    onSurface = SayurboxTextPrimary,
    surfaceVariant = SayurboxSurface,
    onSurfaceVariant = SayurboxTextSecondary,

    outline = SayurboxTextHint,
    outlineVariant = SayurboxTextHint.copy(alpha = 0.5f),

    inverseSurface = SayurboxTextPrimary,
    inverseOnSurface = Color.White,
    inversePrimary = SayurboxLightGreen,

    surfaceTint = SayurboxGreen
)

private val SayurboxDarkColorScheme = darkColorScheme(
    primary = SayurboxGreen,
    onPrimary = SayurboxDarkGreen,
    primaryContainer = SayurboxDarkGreen,
    onPrimaryContainer = SayurboxLightGreen,

    secondary = SayurboxLightGreen,
    onSecondary = SayurboxDarkGreen,
    secondaryContainer = SayurboxDarkGreen.copy(alpha = 0.7f),
    onSecondaryContainer = SayurboxLightGreen,

    tertiary = SayurboxLightGreen,
    onTertiary = SayurboxDarkGreen,
    tertiaryContainer = SayurboxDarkGreen.copy(alpha = 0.5f),
    onTertiaryContainer = SayurboxLightGreen,

    error = SayurboxError.copy(alpha = 0.8f),
    onError = Color.White,
    errorContainer = SayurboxError.copy(alpha = 0.2f),
    onErrorContainer = SayurboxError.copy(alpha = 0.8f),

    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFBDBDBD),

    outline = Color(0xFF757575),
    outlineVariant = Color(0xFF424242),

    inverseSurface = Color(0xFFE0E0E0),
    inverseOnSurface = Color(0xFF121212),
    inversePrimary = SayurboxDarkGreen,

    surfaceTint = SayurboxGreen
)

@Composable
fun SayurboxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> SayurboxDarkColorScheme
        else -> SayurboxLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}