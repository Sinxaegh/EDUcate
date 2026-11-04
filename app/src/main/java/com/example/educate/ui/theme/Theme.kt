package com.example.educate.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = EducateNavy,
    onPrimary = EducateSurface,
    primaryContainer = EducateContainerBlue,
    onPrimaryContainer = EducateNavyDark,
    secondary = EducateAccentBlue,
    onSecondary = EducateSurface,
    background = EducateBlueBg,
    onBackground = EducateTextPrimary,
    surface = EducateSurface,
    onSurface = EducateTextPrimary,
    surfaceVariant = EducateContainerBlue,
    onSurfaceVariant = EducateTextSecondary,
    outline = EducateBorder
)

@Composable
fun EDUcateTheme(
    darkTheme: Boolean = false, // Enforcing clean design matching the mockups
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
