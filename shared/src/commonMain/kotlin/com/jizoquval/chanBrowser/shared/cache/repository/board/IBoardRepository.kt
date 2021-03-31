package com.jizoquval.chanBrowser.shared.cache.repository.board

import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.network.json.BoardJson
import kotlinx.coroutines.flow.Flow

interface IBoardRepository {

    suspend fun insetBoards(chan: Chan, boards: List<BoardJson>)

    fun selectAllBoardsFor(chan: Chan): Flow<List<Board>>
}