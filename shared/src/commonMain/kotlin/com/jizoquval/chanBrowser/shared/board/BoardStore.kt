package com.jizoquval.chanBrowser.shared.board

import com.arkivanov.mvikotlin.core.store.Store
import com.jizoquval.chanBrowser.shared.board.BoardStore.Intent
import com.jizoquval.chanBrowser.shared.board.BoardStore.State
import com.jizoquval.chanBrowser.shared.board.BoardStore.Label
import com.jizoquval.chanBrowser.shared.cache.ThreadPost

interface BoardStore: Store<Intent, State, Label> {

    sealed class Intent {
        class SelectThread(val id: String) : Intent()
    }

    data class State(
        val boardName: String,
        val isProgress: Boolean,
        val threads: List<ThreadPost> = emptyList()
    )

    sealed class Label {
        class NavigateToThread(val threadId: String) : Label()
    }
}