package com.jizoquval.chanBrowser.shared.test.cache

import com.jizoquval.chanBrowser.shared.cache.models.BoardDto
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val board1 = BoardDto(
    chan = Chan.DvaCh,
    idOnChan = "board_2ch_id",
    name = "b_name_1",
    category = "b_cat_1"
)
private val board2 = BoardDto(
    chan = Chan.FourChan,
    idOnChan = "board_4ch_id",
    name = "b_name",
    category = "b_cat_2"
)
private val board3 = BoardDto(
    chan = Chan.FourChan,
    idOnChan = "board_4ch_id_2",
    name = "b_name",
    category = "b_cat_2"
)

class BoardsSelectTest : BaseDbTest() {

    @BeforeTest
    override fun setup() = runTest {
        super.setup()
        boardsDb.insetBoards(listOf(board1, board2))
    }

    @Test
    fun selectAllRowsForDvaChBoards() = runTest {
        val listBoards = boardsDb.selectAllBoardsFor(Chan.DvaCh).firstOrNull()
        assertTrue("expect only one row") { listBoards != null && listBoards.size == 1 }
        val board = listBoards!!.first()
        val idOnChan = threadsDB.selectBoardIdOnChan(board.id)
        assertEquals(board1.category, board.category)
        assertEquals(board1.idOnChan, idOnChan)
        assertEquals(board1.name, board.name)
        assertEquals(Chan.DvaCh, board.chan)
        assertEquals(false, board.isFavorite)
    }

    @Test
    fun selectAllRowsForFourChanBoards() = runTest {
        val listBoards = boardsDb.selectAllBoardsFor(Chan.FourChan).firstOrNull()
        assertTrue("expect only one row") { listBoards != null && listBoards.size == 1 }
        val board = listBoards!!.first()
        val idOnChan = threadsDB.selectBoardIdOnChan(board.id)
        assertEquals(board2.category, board.category)
        assertEquals(board2.idOnChan, idOnChan)
        assertEquals(board2.name, board.name)
        assertEquals(Chan.FourChan, board.chan)
        assertEquals(false, board.isFavorite)
    }

    @Test
    fun upsertTest() = runTest {
        val updatedBoard1 = board1.copy(category = "updated_category")

        val selectedB2 = boardsDb.selectAllBoardsFor(Chan.FourChan).first().first()

        boardsDb.insetBoards(listOf(updatedBoard1, board2, board3))

        val selectedB1au = boardsDb.selectAllBoardsFor(Chan.DvaCh)
            .first()
            .also { assertTrue { it.size == 1 } }
            .first()
        val selectedB2au = boardsDb.selectAllBoardsFor(Chan.FourChan)
            .first()
            .also { assertTrue { it.size == 2 } }
            .also {
                val board = it[1]
                assertEquals(board3.category, board.category)
                assertEquals(board3.name, board.name)
                assertEquals(Chan.FourChan, board.chan)
                assertEquals(false, board.isFavorite)
            }
            .first()

        assertEquals(selectedB2, selectedB2au, "Board must be same after insert or update")

        assertEquals(updatedBoard1.category, selectedB1au.category)
        assertEquals(updatedBoard1.name, selectedB1au.name)
        assertEquals(updatedBoard1.chan, selectedB1au.chan)
        assertEquals(false, selectedB1au.isFavorite)
    }
}
