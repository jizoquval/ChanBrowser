package com.jizoquval.chanBrowser.shared.cache.dbWrapper.thread

import co.touchlab.kermit.Kermit
import com.jizoquval.chanBrowser.shared.cache.AppDatabase
import com.jizoquval.chanBrowser.shared.cache.Thread
import com.jizoquval.chanBrowser.shared.cache.ThreadPost
import com.jizoquval.chanBrowser.shared.cache.models.PostDto
import com.jizoquval.chanBrowser.shared.cache.models.ThreadDto
import com.jizoquval.chanBrowser.shared.cache.transactionWithContext
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ThreadDb(
    database: AppDatabase,
    private val logger: Kermit,
    private val backgroundDispatcher: CoroutineDispatcher
) : IThreadDb {

    private val threadQueries = database.threadQueries
    private val postQuery = database.postQueries

    override suspend fun insert(threadsDto: List<ThreadDto>) {
        threadQueries.transactionWithContext(backgroundDispatcher) {
            afterCommit {
                logger.d { "Insert ${threadsDto.size} threads to database" }
            }
            threadsDto.forEach { dto ->
                updateOrInsertThread(dto)
                updateOrInsertPost(dto.boardId, dto.post)
            }
        }
    }

    override suspend fun selectBoardIdOnChan(boardId: Long): String {
        return withContext(backgroundDispatcher) {
            threadQueries.selectBoardName(boardId).executeAsOne()
        }
    }

    override fun selectThreads(
        boardId: Long,
        offset: Long,
        rowCount: Long
    ): Flow<List<Thread>> = threadQueries.selectThreadsForBoard(boardId, offset, rowCount)
        .asFlow()
        .mapToList()
        .flowOn(backgroundDispatcher)

    override fun selectThreads(
        boardId: Long,
    ): Flow<List<ThreadPost>> = threadQueries.selectAllThreadsForBoard(boardId)
        .asFlow()
        .mapToList()
        .flowOn(backgroundDispatcher)

    private fun updateOrInsertThread(dto: ThreadDto) {
        threadQueries.updateOrInsert(
            maxPostLength = dto.maxMsgLength,
            postsCount = dto.postsCount,
            filesCount = dto.filesCount,
            timestamp = dto.timestamp,
            isEndless = dto.isEndless,
            isArchive = dto.isArchived,
            isClosed = dto.isClosed,
            boardId = dto.boardId,
            idOnChan = dto.threadChanId
        )
    }

    private fun updateOrInsertPost(boardId: Long, dto: PostDto) {
        val threadId = threadQueries.selectThreadId(boardId, dto.postChanId).executeAsOne()
        postQuery.updateOrInsert(
            title = dto.title,
            message = dto.message,
            date = dto.date,
            timestamp = dto.timestamp,
            isOp = dto.isOP,
            isSticky = dto.isSticky,
            authorName = dto.authorName,
            authorEmil = dto.authorEmail,
            likesCount = dto.likesCount,
            dislikesCount = dto.dislikesCount,
            threadId = threadId,
            idOnChan = dto.postChanId
        )
    }
}
