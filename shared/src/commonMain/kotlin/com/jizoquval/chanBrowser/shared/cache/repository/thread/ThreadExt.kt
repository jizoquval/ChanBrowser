package com.jizoquval.chanBrowser.shared.cache.repository.thread

import com.jizoquval.chanBrowser.shared.cache.Thread
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.network.json.ThreadJson
import com.jizoquval.chanBrowser.shared.utils.toBoolean

// TODO remove default isArchive
fun threadFromJson(
    chan: Chan,
    boardId: String,
    postMessageLength: Int,
    isArchive: Boolean = false,
    json: ThreadJson
): Thread = Thread(
    id = json.id,
    chan = chan,
    boardId = boardId,
    postsCount = json.postsCount,
    filesCount = json.filesCount,
    postMessageLength = postMessageLength,
    isEndless = json.isEndLess.toBoolean(),
    isArchive = isArchive,
    isClosed = json.isClosed.toBoolean(),
    timestamp = json.timestamp
)