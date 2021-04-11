package com.jizoquval.chanBrowser.androidApp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jizoquval.chanBrowser.androidApp.ui.utils.BaseAppTheme

@Composable
fun ThreadScreen(
    threadId: String,
    pressUp: () -> Unit
) {
    BaseAppTheme {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { pressUp() }) {
                Text(text = "back to board")
            }
        }
    }
}
