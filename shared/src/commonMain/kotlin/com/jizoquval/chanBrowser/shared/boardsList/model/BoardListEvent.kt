package com.jizoquval.chanBrowser.shared.boardsList.model

sealed class BoardListEvent {
    class SelectFavoriteBoard(val name: String) : BoardListEvent()
    class SelectBoard(val name: String) : BoardListEvent()
}


