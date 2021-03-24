package com.jizoquval.chanBrowser.androidApp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jizoquval.chanBrowser.androidApp.ui.utils.BaseAppTheme

@Composable
fun BoardScreen(
    boardId: String,
    toThread: (String) -> Unit,
    pressUp: () -> Unit
) {
    BaseAppTheme {
        Scaffold(
            topBar = { AppBar() }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                //verticalArrangement = Arrangement.Center,

            ) {
                Text(text = "ID: $boardId")
                Button(onClick = { toThread("THREAD IDDD") }) {
                    Text(text = "Navigate to thread")
                }
            }
        }
    }
}

@Composable
private fun AppBar() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Red)
    ) {
        Text("XXX")
//        Image(
//            painter = painterResource(id = OwlTheme.images.lockupLogo),
//            contentDescription = null,
//            modifier = Modifier.padding(16.dp)
//        )
        IconButton(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = { /* todo */ }
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "settings"
            )
        }
    }
}