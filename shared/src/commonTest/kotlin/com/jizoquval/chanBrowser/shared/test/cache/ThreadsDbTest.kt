package com.jizoquval.chanBrowser.shared.test.cache

import co.touchlab.kermit.Kermit
import com.jizoquval.chanBrowser.shared.cache.AppDatabase
import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.Post
import com.jizoquval.chanBrowser.shared.cache.Thread
import com.jizoquval.chanBrowser.shared.network.json.BoardJson
import com.jizoquval.chanBrowser.shared.test.BaseTest
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.cache.repository.board.BoardRepository
import com.jizoquval.chanBrowser.shared.test.testDbConnection
import com.squareup.sqldelight.EnumColumnAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ThreadsDbTest : BaseTest() {

    private val json1 = BoardJson(
        bumpLimit = 500,
        category = "category1",
        defaultName = "defName",
        id = "id1",
        name = "name1",
        pages = 10,
        sage = 1,
        tripCodes = 1,
        enableDices = 1,
        enableFlags = 1,
        enableLikes = 0,
        enableIcons = 1,
        enableNames = 0,
        enableOekaki = 1,
        enablePosting = 1,
        enableSage = 1,
        enableShield = 0,
        enableSubject = 1,
        enableThreadTags = 0,
        enableTrips = 0,
        icons = null
    )
    private val json2 = BoardJson(
        bumpLimit = 500,
        category = "category2",
        defaultName = "defName2",
        id = "id2",
        name = "name2",
        pages = 10,
        sage = 1,
        tripCodes = 1,
        enableDices = 1,
        enableFlags = 1,
        enableLikes = 0,
        enableIcons = 1,
        enableNames = 0,
        enableOekaki = 1,
        enablePosting = 1,
        enableSage = 1,
        enableShield = 0,
        enableSubject = 1,
        enableThreadTags = 0,
        enableTrips = 0,
        icons = null
    )
    private lateinit var repository: BoardRepository

    @BeforeTest
    fun setup() = runTest {
        repository = BoardRepository(
            AppDatabase(
                testDbConnection(),
                BoardAdapter = Board.Adapter(
                    chanAdapter = EnumColumnAdapter()
                ),
                ThreadAdapter = Thread.Adapter(
                    chanAdapter = EnumColumnAdapter()
                ),
                PostAdapter = Post.Adapter(
                    chanAdapter = EnumColumnAdapter()
                )
            ),
            logger = Kermit(),
            Dispatchers.Default
        ).also {
            it.insetBoards(
                Chan.DvaCh,
                listOf(json1)
            )
            it.insetBoards(
                Chan.FourChan,
                listOf(json2)
            )
        }
    }

    @Test
    fun selectAllRowsForDvaChBoards() = runTest {
        val listBoards = repository.selectAllBoardsFor(Chan.DvaCh).firstOrNull()
        assertTrue("expect only one row") { listBoards != null && listBoards.size == 1 }
        val board = listBoards!!.first()
        assertEquals(json1.category, board.category)
        assertEquals(json1.id, board.id)
        assertEquals(json1.defaultName, board.defaultName)
        assertEquals(json1.name, board.name)
        assertEquals(Chan.DvaCh, board.chan)
        assertEquals(false, board.isFavorite)
    }

    @Test
    fun selectAllRowsForFourChanBoards() = runTest {
        val listBoards = repository.selectAllBoardsFor(Chan.FourChan).firstOrNull()
        assertTrue("expect only one row") { listBoards != null && listBoards.size == 1 }
        val board = listBoards!!.first()
        assertEquals(json2.category, board.category)
        assertEquals(json2.id, board.id)
        assertEquals(json2.defaultName, board.defaultName)
        assertEquals(json2.name, board.name)
        assertEquals(Chan.FourChan, board.chan)
        assertEquals(false, board.isFavorite)
    }
}