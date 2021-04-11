package com.jizoquval.chanBrowser.shared.board.uiModel

import com.jizoquval.chanBrowser.shared.board.BoardStore

fun stateToModel(state: BoardStore.State): BoardModel {
    return BoardModel(
        boardName = state.boardName,
        isProgress = state.isProgress,
        threadList = state.threads.map { thread ->
            BoardModel.Thread(
                id = thread.threadId,
                title = thread.title,
                message = thread.message,
                postsCount = thread.postsCount,
                filesCount = thread.filesCount
            )
        }
    )
}
