package com.jizoquval.chanBrowser.shared.boardsList

import com.arkivanov.mvikotlin.core.store.Store
import com.jizoquval.chanBrowser.shared.cache.Board

interface BoardsListStore: Store<BoardsListStore.Intent, BoardsListStore.State, Nothing> {

    sealed class Intent {
        class BoardSelected(val name: String) : Intent()
    }

    data class State(
        val favorites: List<Board>?,
        val boards: List<Board>?,
        val isProgress: Boolean
    )
}