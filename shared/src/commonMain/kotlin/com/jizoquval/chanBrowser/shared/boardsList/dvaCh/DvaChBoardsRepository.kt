package com.jizoquval.chanBrowser.shared.boardsList.dvaCh

import com.jizoquval.chanBrowser.shared.boardsList.ChanBoardsRepository
import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.models.BoardDto
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.cache.repository.board.IBoardRepository
import com.jizoquval.chanBrowser.shared.network.dvach.IDvachApi
import com.jizoquval.chanBrowser.shared.network.json.BoardJson
import kotlinx.coroutines.flow.Flow

class DvaChBoardsRepository(
    private val api: IDvachApi,
    private val db: IBoardRepository
) : ChanBoardsRepository {

    override suspend fun loadBoards() {
        val boardsDto = api.getBoards().values.flatten().map { it.toDto() }
        db.insetBoards(boardsDto)
    }

    override suspend fun setFavorite(boardId: Long, isFavorite: Boolean) {
        db.setIsFavorite(boardId = boardId, isFavorite = isFavorite)
    }

    override fun subscribeToBoards(): Flow<List<Board>> = db.selectAllBoardsFor(Chan.DvaCh)
}

private fun BoardJson.toDto() = BoardDto(
    chan = Chan.DvaCh,
    idOnChan = id,
    name = name,
    category = category
)