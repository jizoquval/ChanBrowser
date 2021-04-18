package com.jizoquval.chanBrowser.shared.features.boardsList.model

import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.features.boardsList.BoardsListStore

fun stateToModel(state: BoardsListStore.State): BoardListModel {
    val categories = state.boards?.map { it.category }?.toSet()?.map { cName ->
        cName to state.boards.filter { it.category == cName }.map { board ->
            BoardListModel.Board(
                id = board.id,
                name = board.idOnChan,
                description = board.name
            )
        }
    }?.toMap() ?: emptyMap()

    val favoritesViewModel = state.favorites?.map { model ->
        BoardListModel.Board(
            id = model.id,
            name = model.idOnChan,
            description = model.name
        )
    } ?: emptyList()

    val chan = when (state.chan) {
        Chan.DvaCh -> "2ch"
        Chan.FourChan -> "4chan"
    }

    return BoardListModel(
        chan = chan,
        categories = categories,
        favorites = favoritesViewModel,
        isProgress = state.isProgress
    )
}

fun eventToIntent(event: BoardListEvent): BoardsListStore.Intent {
    return when (event) {
        is BoardListEvent.SelectFavoriteBoard -> BoardsListStore.Intent.SelectFavorite(id = event.boardId)
        is BoardListEvent.SelectBoard -> BoardsListStore.Intent.BoardSelected(id = event.boardId)
    }
}
