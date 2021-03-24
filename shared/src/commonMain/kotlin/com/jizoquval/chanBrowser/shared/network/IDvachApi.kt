package com.jizoquval.chanBrowser.shared.network

import com.jizoquval.chanBrowser.shared.network.json.BoardJson

interface IDvachApi {

    suspend fun getBoards(): Map<String, List<BoardJson>>
    suspend fun getThreads(boardName: String): String
}