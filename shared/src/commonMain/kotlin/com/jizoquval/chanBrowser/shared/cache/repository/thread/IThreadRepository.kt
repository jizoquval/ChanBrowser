package com.jizoquval.chanBrowser.shared.cache.repository.thread

import com.jizoquval.chanBrowser.shared.cache.Thread
import com.jizoquval.chanBrowser.shared.cache.ThreadPost
import com.jizoquval.chanBrowser.shared.network.json.ThreadsListJson
import kotlinx.coroutines.flow.Flow

interface IThreadRepository {

    suspend fun insert(boardId: Long, threadJson: ThreadsListJson)
    suspend fun selectBoardIdOnChan(boardId: Long): String
    fun selectThreads(boardId: Long, offset: Long, rowCount: Long): Flow<List<Thread>>
    fun selectThreads(boardId: Long): Flow<List<ThreadPost>>
}
