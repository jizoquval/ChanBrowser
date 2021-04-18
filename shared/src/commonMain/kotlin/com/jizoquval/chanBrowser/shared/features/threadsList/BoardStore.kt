package com.jizoquval.chanBrowser.shared.features.threadsList

import com.arkivanov.mvikotlin.core.store.Store
import com.jizoquval.chanBrowser.shared.cache.ThreadPost
import com.jizoquval.chanBrowser.shared.features.threadsList.BoardStore.Intent
import com.jizoquval.chanBrowser.shared.features.threadsList.BoardStore.Label
import com.jizoquval.chanBrowser.shared.features.threadsList.BoardStore.State

interface BoardStore : Store<Intent, State, Label> {

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
