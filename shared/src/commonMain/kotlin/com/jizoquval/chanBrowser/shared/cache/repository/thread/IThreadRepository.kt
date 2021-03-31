package com.jizoquval.chanBrowser.shared.cache.repository.thread

import com.jizoquval.chanBrowser.shared.cache.Thread
import com.jizoquval.chanBrowser.shared.cache.ThreadPost
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.network.json.ThreadJson
import com.jizoquval.chanBrowser.shared.network.json.ThreadsListJson
import kotlinx.coroutines.flow.Flow

interface IThreadRepository {

    suspend fun insert(chan: Chan, boardId: String, threadJson: ThreadsListJson)
    fun selectThreads(chan: Chan, boardId: String, offset: Long, rowCount: Long): Flow<List<Thread>>
    fun selectThreads(chan: Chan, boardId: String): Flow<List<ThreadPost>>
}