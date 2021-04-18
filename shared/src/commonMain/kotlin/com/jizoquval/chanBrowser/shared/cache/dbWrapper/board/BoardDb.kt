package com.jizoquval.chanBrowser.shared.cache.dbWrapper.board

import co.touchlab.kermit.Kermit
import com.jizoquval.chanBrowser.shared.cache.AppDatabase
import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.models.BoardDto
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.cache.transactionWithContext
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class BoardDb(
    database: AppDatabase,
    private val logger: Kermit,
    private val backgroundDispatcher: CoroutineDispatcher
) : IBoardDb {

    private val dbQuery = database.boardQueries

    override suspend fun insetBoards(boards: List<BoardDto>) {
        dbQuery.transactionWithContext(backgroundDispatcher) {
            afterCommit {
                logger.d { "Insert ${boards.size} boards to database" }
            }
            boards.forEach { dto ->
                updateOrInsert(dto)
            }
        }
    }

    override suspend fun setIsFavorite(boardId: Long, isFavorite: Boolean) {
        dbQuery.transactionWithContext(backgroundDispatcher) {
            dbQuery.setIsFavorite(isFavorite = isFavorite, id = boardId)
        }
    }

    override fun selectAllBoardsFor(chan: Chan): Flow<List<Board>> {
        return dbQuery.selectAllForChan(chan).asFlow().mapToList().flowOn(backgroundDispatcher)
    }

    private fun updateOrInsert(dto: BoardDto) {
        dbQuery.transaction {
            dbQuery.updateBoard(
                name = dto.name,
                category = dto.category,
                chan = dto.chan,
                idOnChan = dto.idOnChan
            )
            dbQuery.insertBoard(
                idOnChan = dto.idOnChan,
                chan = dto.chan,
                name = dto.name,
                category = dto.category
            )
        }
    }
}
