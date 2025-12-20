package com.espol.gummyapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = GomiPrimary,
    onPrimary = Color.White,

    secondary = GomiGreen,
    onSecondary = Color.White,

    tertiary = GomiPrimaryLight,
    onTertiary = GomiTextPrimary,

    background = GomiBackground,
    onBackground = GomiTextPrimary,

    surface = GomiBackgroundAlt,
    onSurface = GomiTextPrimary,

    error = GomiError,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = GomiPrimary,
    onPrimary = Color.White,

    secondary = GomiGreen,
    onSecondary = Color.White,

    tertiary = GomiPrimaryLight,
    onTertiary = GomiTextPrimary,

    background = Color(0xFF1B1B2F),
    onBackground = Color.White,

    surface = Color(0xFF2A2A40),
    onSurface = Color.White,

    error = GomiError,
    onError = Color.White
)

@Composable
fun GummyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
