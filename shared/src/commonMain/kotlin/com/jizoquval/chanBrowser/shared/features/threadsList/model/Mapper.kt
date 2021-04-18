package com.jizoquval.chanBrowser.shared.features.threadsList.model

import com.jizoquval.chanBrowser.shared.features.threadsList.BoardStore

fun stateToModel(state: BoardStore.State): BoardModel {
    return BoardModel(
        boardName = state.boardName,
        isProgress = state.isProgress,
        threadList = state.threads.map { thread ->
            BoardModel.Thread(
                id = thread.idOnChan,
                title = thread.title,
                imgUrl = thread.smallAttachmentPath,
                message = thread.message,
                postsCount = thread.postsCount,
                filesCount = thread.filesCount
            )
        }
    )
}
