package com.jizoquval.chanBrowser.shared.test.cache

import co.touchlab.kermit.Kermit
import com.jizoquval.chanBrowser.shared.cache.AppDatabase
import com.jizoquval.chanBrowser.shared.cache.Attachment
import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.dbWrapper.board.BoardDb
import com.jizoquval.chanBrowser.shared.cache.dbWrapper.thread.ThreadDb
import com.jizoquval.chanBrowser.shared.test.BaseTest
import com.jizoquval.chanBrowser.shared.test.testDbConnection
import com.squareup.sqldelight.EnumColumnAdapter
import kotlinx.coroutines.Dispatchers
import kotlin.test.BeforeTest

open class BaseDbTest : BaseTest() {
    private val logger = Kermit()

    protected lateinit var boardsDb: BoardDb
    protected lateinit var threadsDB: ThreadDb

    @BeforeTest
    open fun setup() = runTest {
        val db = AppDatabase(
            testDbConnection(),
            BoardAdapter = Board.Adapter(
                chanAdapter = EnumColumnAdapter()
            ),
            AttachmentAdapter = Attachment.Adapter(
                typeAdapter = EnumColumnAdapter()
            )
        )
        boardsDb = BoardDb(
            database = db,
            logger = logger,
            Dispatchers.Default
        )

        threadsDB = ThreadDb(
            db,
            logger = logger,
            Dispatchers.Default
        )
    }
}