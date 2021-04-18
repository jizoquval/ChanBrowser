package com.jizoquval.chanBrowser.shared.features.boardsList

import com.jizoquval.chanBrowser.shared.cache.Board
import kotlinx.coroutines.flow.Flow

interface ChanBoardsRepository {
    suspend fun loadBoards()
    suspend fun setFavorite(boardId: Long, isFavorite: Boolean)
    fun subscribeToBoards(): Flow<List<Board>>
}
