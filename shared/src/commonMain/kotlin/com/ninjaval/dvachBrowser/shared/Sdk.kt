package com.ninjaval.dvachBrowser.shared

import com.ninjaval.dvachBrowser.shared.cache.Database
import com.ninjaval.dvachBrowser.shared.cache.DatabaseDriverFactory
import com.ninjaval.dvachBrowser.shared.network.DvachApi
import com.ninjaval.dvachBrowser.shared.network.pojo.BoardJson

class Sdk(driverFactory: DatabaseDriverFactory) {
    private val database = Database(driverFactory)
    private val api = DvachApi()

    fun insert() {
        database.insetBoard()
    }

    suspend fun getBoards(): Map<String, List<BoardJson>> {
        return api.getBoards()
    }
}