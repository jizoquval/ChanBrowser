package com.jizoquval.chanBrowser.shared.cache.dbWrapper.thread

import com.jizoquval.chanBrowser.shared.cache.Thread
import com.jizoquval.chanBrowser.shared.cache.ThreadPost
import com.jizoquval.chanBrowser.shared.cache.models.ThreadDto
import kotlinx.coroutines.flow.Flow

interface IThreadDb {

    suspend fun insert(threadsDto: List<ThreadDto>)
    suspend fun selectBoardIdOnChan(boardId: Long): String
    fun selectThreads(boardId: Long, offset: Long, rowCount: Long): Flow<List<Thread>>
    fun selectThreads(boardId: Long): Flow<List<ThreadPost>>
}
