package com.jizoquval.chanBrowser.shared.cache.repository.thread

import com.jizoquval.chanBrowser.shared.cache.AppDatabase
import com.jizoquval.chanBrowser.shared.cache.Thread
import com.jizoquval.chanBrowser.shared.cache.ThreadPost
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.cache.repository.post.postFromThreadJson
import com.jizoquval.chanBrowser.shared.cache.transactionWithContext
import com.jizoquval.chanBrowser.shared.logger.log
import com.jizoquval.chanBrowser.shared.network.json.ThreadsListJson
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class ThreadRepository(
    database: AppDatabase,
    private val backgroundDispatcher: CoroutineDispatcher
) : IThreadRepository {

    private val threadQueries = database.threadQueries
    private val postQuery = database.postQueries

    override suspend fun insert(chan: Chan, boardId: String, threadJson: ThreadsListJson) {
        val postMaxLength = threadJson.lengthOfMessage
        threadQueries.transactionWithContext(backgroundDispatcher) {
            afterCommit {
                log("Insert ${threadJson.threads.size} threads to database")
            }
            threadJson.threads.map { json ->
                threadFromJson(
                    chan = chan,
                    boardId = boardId,
                    json = json,
                    postMessageLength = postMaxLength
                ) to postFromThreadJson(chan, json)
            }.forEach { threadPost ->
                threadQueries.putThread(threadPost.first)
                postQuery.putPost(threadPost.second)
            }
        }
    }

    override fun selectThreads(
        chan: Chan,
        boardId: String,
        offset: Long,
        rowCount: Long
    ): Flow<List<Thread>> = threadQueries.selectThreadsForBoard(chan, boardId, offset, rowCount)
        .asFlow()
        .mapToList()
        .flowOn(backgroundDispatcher)

    override fun selectThreads(
        chan: Chan,
        boardId: String,
    ): Flow<List<ThreadPost>> = threadQueries.selectAllThreadsForBoard(chan, boardId)
        .asFlow()
        .mapToList()
        .flowOn(backgroundDispatcher)
}
