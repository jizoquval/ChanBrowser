package com.jizoquval.chanBrowser.shared.cache.dbWrapper.board

import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.models.BoardDto
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import kotlinx.coroutines.flow.Flow

interface IBoardDb {

    suspend fun insetBoards(boards: List<BoardDto>)
    suspend fun setIsFavorite(boardId: Long, isFavorite: Boolean)
    fun selectAllBoardsFor(chan: Chan): Flow<List<Board>>
}
