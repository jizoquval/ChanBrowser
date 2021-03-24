package com.jizoquval.chanBrowser.shared.boardsList.model

import com.jizoquval.chanBrowser.shared.boardsList.BoardsListStore

inline fun stateToModel(state: BoardsListStore.State): BoardListModel {
    val categories = state.boards?.map { it.category }?.toSet()?.map { cName ->
        cName to state.boards.filter { it.category == cName }.map { board ->
            BoardListModel.Board(
                name = board.chanId,
                description = board.name
            )
        }
    }?.toMap() ?: emptyMap()

    val favoritesViewModel = state.favorites?.map { model ->
        BoardListModel.Board(
            name = model.chanId,
            description = model.name
        )
    } ?: emptyList()

    return BoardListModel(
        categories = categories,
        favorites = favoritesViewModel,
        isProgress = state.isProgress
    )
}

inline fun eventToIntent(event: BoardListEvent): BoardsListStore.Intent {
    return when (event) {
        is BoardListEvent.BoardSelected -> BoardsListStore.Intent.BoardSelected(name = event.name)
    }
}