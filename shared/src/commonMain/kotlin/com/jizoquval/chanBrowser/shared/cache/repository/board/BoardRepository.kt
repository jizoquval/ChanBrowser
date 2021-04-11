package com.jizoquval.chanBrowser.shared.cache.repository.board

import co.touchlab.kermit.Kermit
import com.jizoquval.chanBrowser.shared.cache.AppDatabase
import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.models.Chan

import com.jizoquval.chanBrowser.shared.cache.transactionWithContext
import com.jizoquval.chanBrowser.shared.network.json.BoardJson
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class BoardRepository(
    private val database: AppDatabase,
    private val logger: Kermit,
    private val backgroundDispatcher: CoroutineDispatcher
) : IBoardRepository {

    private val dbQuery = database.boardQueries

    override suspend fun insetBoards(chan: Chan, boards: List<BoardJson>) {
        database.transactionWithContext(backgroundDispatcher) {
            afterCommit {
                logger.d { "Insert ${boards.size} boards to database" }
            }
            boards.forEach { v ->
                dbQuery.putBoard(
                    chan = chan,
                    id = v.id,
                    defaultName = v.defaultName,
                    name = v.name,
                    category = v.category,
                )
            }
        }
    }

    override fun selectAllBoardsFor(chan: Chan): Flow<List<Board>> {
        return dbQuery.selectAllForChan(chan).asFlow().mapToList().flowOn(backgroundDispatcher)
    }
}
