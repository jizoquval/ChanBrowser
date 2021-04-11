package com.jizoquval.chanBrowser.androidApp.ui.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// TODO ADD COlORS
private val DarkColorPalette = darkColors(
    primary = yellow500,
    primaryVariant = yellow800,
    onPrimary = Color.White,

    secondary = gray800,
    secondaryVariant = Color.Cyan,
    onSecondary = Color.White,

    background = darkGrayBG,
    onBackground = Color.White,

    surface = ashGray,
    onSurface = lightGray
    // surface = darkGrayBG,
    // background = darkGrayBG,
)

private val LightColorPalette = lightColors(
    primary = yellow500,
    primaryVariant = yellow800,
    secondary = gray800,
    onPrimary = Color.Black,
    onSurface = Color.Black
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
