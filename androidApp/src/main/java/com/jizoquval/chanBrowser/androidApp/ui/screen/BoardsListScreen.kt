package com.jizoquval.chanBrowser.androidApp.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jizoquval.chanBrowser.androidApp.R
import com.jizoquval.chanBrowser.androidApp.ui.utils.BaseAppTheme
import com.jizoquval.chanBrowser.shared.boardsList.model.BoardListEvent
import com.jizoquval.chanBrowser.shared.boardsList.model.BoardListModel
import com.jizoquval.chanBrowser.shared.viewModel.BoardsListViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun BoardsListScreen(
    toBoard: (String) -> Unit,
    toSettings: () -> Unit,
    boardsListViewModel: BoardsListViewModel = getViewModel()
) {
    val state = boardsListViewModel.state.collectAsState()
    val onBoardSelect =
        { board: String -> boardsListViewModel.dispatch(BoardListEvent.BoardSelected(board)) }
    val selectedApp = remember { "4chan" }

    BaseAppTheme {
        Scaffold(
            topBar = { TopBoardsBar(selectedApp = selectedApp, toSettings = toSettings) }
        ) { innerPadding ->
            Column(
                Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 12.dp)
            ) {
                if (state.value.favorites.isNotEmpty()) {
                    Favorites(favoritesBoards = state.value.favorites, toBoard = toBoard)
                }
                Boards(boardsCategories = state.value.categories, toBoard = onBoardSelect)
            }
        }
    }
}

@Composable
private fun Favorites(
    favoritesBoards: List<BoardListModel.Board>,
    toBoard: (String) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.favorites),
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(items = favoritesBoards) { board ->
                BoardCardShort(board = board, toBoard = toBoard)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Boards(
    boardsCategories: Map<String, List<BoardListModel.Board>>,
    toBoard: (String) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.boards),
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            boardsCategories.forEach { (category, boardsList) ->
                stickyHeader {
                    Card(
                        modifier = Modifier.fillMaxWidth().height(33.dp),
                        elevation = 5.dp,
                        shape = MaterialTheme.shapes.large,
                        backgroundColor = MaterialTheme.colors.background
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.onSurface,
                        )
                    }
                }
                items(items = boardsList) { board ->
                    BoardCardLong(board = board, toBoard = toBoard)
                }
            }
        }
    }
}

@Composable
private fun BoardCardShort(
    board: BoardListModel.Board,
    toBoard: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(80.dp)
            .clickable { toBoard(board.name) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = board.name,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primary,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun BoardsCategoryBlock(
    category: BoardListModel.Category,
    toBoard: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = category.category,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onSurface
        )
        for (board in category.boards) {
            BoardCardLong(board = board, toBoard = toBoard)
        }
    }
}

@Composable
private fun BoardCardLong(
    board: BoardListModel.Board,
    toBoard: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { toBoard(board.name) }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(
                text = board.name,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primary,
            )
            Text(text = board.description, color = MaterialTheme.colors.onSurface)
        }
    }
}

@Composable
private fun TopBoardsBar(selectedApp: String, toSettings: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = selectedApp,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h3
            )
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.label_search)
                )
            }
            IconButton(onClick = { toSettings() }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.label_settings)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BoardsCategoryBlockPreview() {
    BaseAppTheme(darkTheme = true) {
        BoardsCategoryBlock(
            category = BoardListModel.Category(
                category = "Different",
                boards = listOf(
                    BoardListModel.Board("bbbbbbbb", "Random"),
                    BoardListModel.Board("g", "Thchnology"),
                    BoardListModel.Board("ca", "Animals & Nature"),
                )
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    BaseAppTheme(darkTheme = true) {
        TopBoardsBar(selectedApp = "2ch") {}
    }
}

@Preview(showBackground = true)
@Composable
fun BoardCardShortPreview() {
    BaseAppTheme(darkTheme = true) {
        BoardCardShort(board = BoardListModel.Board("ca", "Animals & Nature")) {}
    }
}