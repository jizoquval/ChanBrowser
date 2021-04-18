package com.jizoquval.chanBrowser.shared.features.boardsList.model

sealed class BoardListEvent {
    class SelectFavoriteBoard(val boardId: Long) : BoardListEvent()
    class SelectBoard(val boardId: Long) : BoardListEvent()
}
