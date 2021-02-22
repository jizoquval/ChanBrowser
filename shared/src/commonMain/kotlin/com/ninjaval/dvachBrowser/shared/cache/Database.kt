package com.ninjaval.dvachBrowser.shared.cache

internal class Database(dbFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(dbFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun insetBoard() {
        val board = Board("b")
        dbQuery.insertFullBoard(board)
    }

    internal fun clearDatabase() {
        with(dbQuery) {
            transaction {
                removeAllBoards()
            }
        }
    }
}