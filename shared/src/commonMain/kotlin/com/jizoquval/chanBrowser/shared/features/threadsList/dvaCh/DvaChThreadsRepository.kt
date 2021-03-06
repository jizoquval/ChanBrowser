package com.jizoquval.chanBrowser.shared.features.threadsList.dvaCh

import com.jizoquval.chanBrowser.shared.cache.ThreadPost
import com.jizoquval.chanBrowser.shared.cache.dbWrapper.thread.IThreadDb
import com.jizoquval.chanBrowser.shared.cache.models.Attachment
import com.jizoquval.chanBrowser.shared.cache.models.AttachmentDto
import com.jizoquval.chanBrowser.shared.cache.models.PostDto
import com.jizoquval.chanBrowser.shared.cache.models.ThreadDto
import com.jizoquval.chanBrowser.shared.features.threadsList.ThreadRepository
import com.jizoquval.chanBrowser.shared.network.Endpoint
import com.jizoquval.chanBrowser.shared.network.dvach.IDvachApi
import com.jizoquval.chanBrowser.shared.network.dvach.json.FileJson
import com.jizoquval.chanBrowser.shared.network.dvach.json.ThreadJson
import com.jizoquval.chanBrowser.shared.utils.toBoolean
import kotlinx.coroutines.flow.Flow

class DvaChThreadsRepository(
    private val api: IDvachApi,
    private val db: IThreadDb,
) : ThreadRepository {

    override suspend fun loadThreads(boardId: Long) {
        val idOnChan = db.selectBoardIdOnChan(boardId)
        val response = api.getThreads(idOnChan)
        val dto = response.threads.map { it.toDto(boardId, response.lengthOfMessage) }
        db.insert(dto)
    }

    override suspend fun getBoardName(boardId: Long): String = db.selectBoardIdOnChan(boardId)

    override fun subscribeToThreads(boardId: Long): Flow<List<ThreadPost>> =
        db.selectThreads(boardId = boardId)
}

private fun ThreadJson.toDto(boardId: Long, maxMessageLength: Int) = ThreadDto(
    boardId = boardId,
    threadChanId = id,
    maxMsgLength = maxMessageLength,
    postsCount = postsCount,
    filesCount = filesCount,
    timestamp = timestamp,
    isEndless = isEndLess.toBoolean(),
    isClosed = isClosed.toBoolean(),
    isArchived = false,
    post = PostDto(
        postChanId = id,
        title = subject,
        message = comment,
        date = date,
        timestamp = timestamp,
        isOP = op.toBoolean(),
        isSticky = isSticky,
        authorName = authorName,
        authorEmail = authorEmail,
        likesCount = likes,
        dislikesCount = dislikes,
        attachments = files.map { it.toDto() }
    )
)

private fun FileJson.toDto() = AttachmentDto(
    url = "https://${Endpoint.DvaCh.url}$path",
    type = Attachment.byCode(type),
    isForAdults = isForAdults.toBoolean(),
    width = width,
    height = height,
    smallFUrl = "https://${Endpoint.DvaCh.url}$smallFilePath",
    smallFWidth = smallFWidth,
    smallFHeight = smallFHeight
)
