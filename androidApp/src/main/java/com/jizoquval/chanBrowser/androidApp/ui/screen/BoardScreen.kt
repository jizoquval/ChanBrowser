package com.jizoquval.chanBrowser.androidApp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jizoquval.chanBrowser.androidApp.R
import com.jizoquval.chanBrowser.androidApp.ui.components.Img
import com.jizoquval.chanBrowser.androidApp.ui.utils.BaseAppTheme
import com.jizoquval.chanBrowser.shared.features.threadsList.model.BoardModel
import com.jizoquval.chanBrowser.shared.viewModel.BoardViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BoardScreen(
    boardId: Long,
    toThread: (Long) -> Unit,
    pressUp: () -> Unit,
    boardViewModel: BoardViewModel = getViewModel(parameters = { parametersOf(boardId) })
) {
    val state = boardViewModel.state.collectAsState()
    BaseAppTheme {
        Scaffold(
            topBar = { TopBar(threadName = state.value.boardName, onBackClick = pressUp) }
        ) { innerPadding ->
            Column(
                Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 12.dp)
                    .padding(top = 8.dp)
            ) {
                ThreadList(state.value.threadList)
            }
        }
    }
}

@Composable
private fun ThreadList(list: List<BoardModel.Thread>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = list) {
            Thread(
                title = it.title,
                message = it.message,
                postsCount = it.postsCount,
                filesFount = it.filesCount,
                image = {
                    Img(
                        img = Img.Remote(url = it.imgUrl),
                        contentDescription = "Thread image",
                        modifier = Modifier.size(85.dp).clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.FillBounds
                    )
                }
            )
        }
    }
}

@Composable
private fun Thread(
    title: String,
    message: String,
    postsCount: Int,
    filesFount: Int,
    image: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.98f).height(113.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(3f)) {
                Text(
                    text = title,
                    maxLines = 1,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h5,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = message,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                image()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Img(
                        img = Img.Res(R.drawable.ic_email),
                        contentDescription = "Count of posts",
                        modifier = Modifier.size(width = 14.dp, height = 14.dp),
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
                    )
                    Text(
                        text = postsCount.toString(),
                        style = MaterialTheme.typography.caption,
                    )
                    Img(
                        img = Img.Res(R.drawable.ic_image),
                        contentDescription = "Count of files",
                        modifier = Modifier.size(width = 14.dp, height = 14.dp),
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
                    )
                    Text(
                        text = filesFount.toString(),
                        style = MaterialTheme.typography.caption,
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(threadName: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "/$threadName",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h3
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.label_back)
                )
            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
    )
}

@Preview(showBackground = true)
@Composable
private fun ThreadPreview() {
    BaseAppTheme(darkTheme = true) {
        Thread(
            title = "My first thread in the world",
            message = "Lorem Ipsum для распечатки образцов. Lorem Ipsum не только успешно пережил без заметных изменений пять веков, но, без заметных изменений пять веков, но , без заметных изменений пять веков, но  ...",
            postsCount = 10,
            filesFount = 3,
            image = {
                Img(
                    img = Img.Res(id = R.drawable.preview),
                    contentDescription = "tmp",
                    modifier = Modifier.clip(MaterialTheme.shapes.medium)
                )
            },
        )
    }
}
