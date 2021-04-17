package com.jizoquval.chanBrowser.shared.network.dvach

import com.jizoquval.chanBrowser.shared.network.json.BoardJson
import com.jizoquval.chanBrowser.shared.network.json.ThreadsListJson

interface IDvachApi {

    suspend fun getBoards(): Map<String, List<BoardJson>>
    suspend fun getThreads(boardIdOnDvaCh: String): ThreadsListJson
}
