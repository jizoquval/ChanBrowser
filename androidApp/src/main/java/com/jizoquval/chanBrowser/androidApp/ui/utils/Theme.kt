package com.jizoquval.chanBrowser.androidApp.ui.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = yellow500,
    primaryVariant = yellow800,
    onPrimary = Color.White,

    secondary = gray800,
    secondaryVariant = Color.Cyan,
    onSecondary = Color.White,

    background = darkGray,
    onBackground = Color.White,

    surface = ashGray,
    onSurface = lightGray
)

private val LightColorPalette = lightColors(
    primary = yellow500,
    primaryVariant = yellow800,
    onPrimary = darkGray,

    secondary = gray800,
    onSecondary = darkGray,

    background = Color.White,
    onBackground = darkGray,

    surface = ltSurfaceWhite,
    onSurface = ltOnSurfaceGray
)

@Composable
fun BaseAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    // val typography = if (darkTheme) DarkPalletTypografy else LightPalletTypografy
    MaterialTheme(
        colors = colors,
        typography = Typografy,
        shapes = shapes,
    ) {
        content()
    }
}
