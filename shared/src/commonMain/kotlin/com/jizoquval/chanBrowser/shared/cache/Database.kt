package com.jizoquval.chanBrowser.shared.cache

import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.logger.log
import com.jizoquval.chanBrowser.shared.network.json.BoardJson
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class Database(
    sdlDriver: SqlDriver,
    private val backgroundDispatcher: CoroutineDispatcher
) {
    private val database = AppDatabase(
        sdlDriver,
        BoardAdapter = Board.Adapter(
            chanAdapter = EnumColumnAdapter()
        )
    )
    private val dbQuery = database.appDatabaseQueries

    suspend fun insetBoards(chan: Chan, boards: List<BoardJson>) {
        log("Insert ${boards.size} boards to database")
        database.transactionWithContext(backgroundDispatcher) {
            boards.forEach { v ->
                dbQuery.putBoard(
                    chan = chan,
                    chanId = v.id,
                    defaultName = v.defaultName,
                    name = v.name,
                    category = v.category,
                )
            }
        }
    }

    fun selectAllBoardsFor(chan: Chan): Flow<List<Board>> {
        return dbQuery.selectAllBoards(chan).asFlow().mapToList().flowOn(backgroundDispatcher)
    }

    fun clearDatabase() {
//        with(dbQuery) {
//            transaction {
//                removeAllBoards()
//            }
//        }
    }
}