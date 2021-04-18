package com.jizoquval.chanBrowser.shared.features.threadsList

import com.jizoquval.chanBrowser.shared.cache.ThreadPost
import kotlinx.coroutines.flow.Flow

interface ThreadRepository {
    suspend fun loadThreads(boardId: Long)
    suspend fun getBoardName(boardId: Long): String
    fun subscribeToThreads(boardId: Long): Flow<List<ThreadPost>>
}
