package com.kastep.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val KastepDarkScheme = darkColorScheme(
    primary = KastepCyan,
    secondary = KastepBlue,
    tertiary = KastepPurple,
    background = KastepBlack,
    surface = KastepDarkSurface,
    surfaceVariant = KastepCardDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = KastepWhite,
    onSurface = KastepWhite,
    onSurfaceVariant = KastepGray,
    outline = KastepCardBorder
)

@Composable
fun KastepTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = KastepBlack.toArgb()
            window.navigationBarColor = KastepBlack.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = KastepDarkScheme,
        typography = Typography,
        content = content
    )
}
