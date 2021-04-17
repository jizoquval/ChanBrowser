package com.jizoquval.chanBrowser.shared.boardsList

import com.arkivanov.mvikotlin.core.store.Store
import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.models.Chan

interface BoardsListStore : Store<BoardsListStore.Intent, BoardsListStore.State, BoardsListStore.Label> {

    sealed class Intent {
        class SelectFavorite(val id: Long) : Intent()
        class BoardSelected(val id: Long) : Intent()
        object SettingsSelected : Intent()
    }

    data class State(
        val favorites: List<Board>?,
        val boards: List<Board>?,
        val isProgress: Boolean,
        val chan: Chan,
        val error: String? = null
    )

    sealed class Label {
        class NavigateToThread(val boardId: Long) : Label()
        object NavigateToSettings : Label()
    }
}
