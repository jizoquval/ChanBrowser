package com.jizoquval.chanBrowser.shared.cache.repository.thread

import co.touchlab.kermit.Kermit
import com.jizoquval.chanBrowser.shared.cache.AppDatabase
import com.jizoquval.chanBrowser.shared.cache.Thread
import com.jizoquval.chanBrowser.shared.cache.ThreadPost
import com.jizoquval.chanBrowser.shared.cache.transactionWithContext
import com.jizoquval.chanBrowser.shared.network.json.ThreadJson
import com.jizoquval.chanBrowser.shared.network.json.ThreadsListJson
import com.jizoquval.chanBrowser.shared.utils.toBoolean
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ThreadRepository(
    database: AppDatabase,
    private val logger: Kermit,
    private val backgroundDispatcher: CoroutineDispatcher
) : IThreadRepository {

    private val threadQueries = database.threadQueries
    private val postQuery = database.postQueries

    override suspend fun insert(boardId: Long, threadJson: ThreadsListJson) {
        threadQueries.transactionWithContext(backgroundDispatcher) {
            afterCommit {
                logger.d { "Insert ${threadJson.threads.size} threads to database" }
            }
            threadJson.threads.forEach { json ->
                updateOrInsertThread(boardId, json, threadJson.lengthOfMessage)
                updateOrInsertPost(boardId, json)
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

    private fun updateOrInsertThread(boardId: Long, json: ThreadJson, maxPostLength: Int) {
        threadQueries.updateThread(
            postMessageLength = maxPostLength,
            postsCount = json.postsCount,
            filesCount = json.filesCount,
            timestamp = json.timestamp,
            isEndless = json.isEndLess.toBoolean(),
            // TODO support archive
            isArchive = false,
            isClosed = json.isClosed.toBoolean(),
            boardId = boardId,
            idOnChan = json.id
        )
        threadQueries.insertThread(
            idOnChan = json.id,
            boardId = boardId,
            postMessageLength = maxPostLength,
            postsCount = json.postsCount,
            filesCount = json.filesCount,
            timestamp = json.timestamp,
            isEndless = json.isEndLess.toBoolean(),
            // TODO support archive
            isArchive = false,
            isClosed = json.isClosed.toBoolean(),
        )
    }

    private fun updateOrInsertPost(boardId: Long, json: ThreadJson) {
        val threadId = threadQueries.selectThreadId(boardId, json.id).executeAsOne()
        postQuery.updatePost(
            title = json.subject,
            message = json.comment,
            date = json.date,
            timestamp = json.timestamp,
            isOP = json.op.toBoolean(),
            isStiky = json.isSticky,
            name = json.authorName,
            email = json.authorEmail,
            likes = json.likes,
            dislikes = json.dislikes,
            threadId = threadId,
            idOnChan = json.id
        )
        postQuery.insertPost(
            idOnChan = json.id,
            threadId = threadId,
            title = json.subject,
            message = json.comment,
            date = json.date,
            timestamp = json.timestamp,
            isOP = json.op.toBoolean(),
            isStiky = json.isSticky,
            name = json.authorName,
            email = json.authorEmail,
            likes = json.likes,
            dislikes = json.dislikes,
        )
    }
}
