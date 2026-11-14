package com.ejemplo.grovespemusic.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = LimeGreen,
    onPrimary = DeepSpace,
    background = DeepSpace,
    onBackground = White,
    surface = DarkPurple,
    onSurface = White,
    secondary = SoftPurple,
    onSecondary = White,
    surfaceVariant = CardBackground,
    onSurfaceVariant = GrayText
)

@Composable
fun GrovespeMusicTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
