package com.jizoquval.chanBrowser.androidApp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.google.accompanist.coil.CoilImage

sealed class Img {
    data class Res(@DrawableRes val id: Int) : Img()
    data class Remote(val url: String) : Img()
}

@Composable
fun Img(
    img: Img,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    fadeIn: Boolean = true
) {

    when (img) {
        is Img.Res -> {
            Image(
                painter = painterResource(id = img.id),
                contentDescription = contentDescription,
                alignment = alignment,
                modifier = modifier,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter
            )
        }
        is Img.Remote -> {
            CoilImage(
                data = img.url,
                contentDescription = contentDescription,
                modifier = modifier,
                alignment = alignment,
                contentScale = contentScale,
                colorFilter = colorFilter,
                fadeIn = fadeIn,
                loading = {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            )
        }
    }
}