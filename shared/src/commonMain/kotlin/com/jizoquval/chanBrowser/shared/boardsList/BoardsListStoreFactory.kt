package com.jizoquval.chanBrowser.shared.boardsList

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.cache.repository.board.IBoardRepository
import com.jizoquval.chanBrowser.shared.logger.LogLevel
import com.jizoquval.chanBrowser.shared.logger.log
import com.jizoquval.chanBrowser.shared.network.dvach.IDvachApi
import io.ktor.client.features.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BoardsListStoreFactory(private val storeFactory: StoreFactory) : KoinComponent {

    private val api: IDvachApi by inject()
    private val db: IBoardRepository by inject()

    private sealed class Action {
        object SubscribeToBoards : Action()
        object CantUpdateBoards : Action()
    }

    private sealed class Result {
        class BoardsList(val boards: List<Board>) : Result()
        class Error(val message: String = "No Internet"): Result()
    }

    fun create(): BoardsListStore =
        object : BoardsListStore,
            Store<BoardsListStore.Intent, BoardsListStore.State, BoardsListStore.Label> by storeFactory.create(
                name = "BoardsListStore",
                initialState = BoardsListStore.State(
                    favorites = null,
                    boards = null,
                    isProgress = true,
                    chan = Chan.DvaCh
                ),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private inner class ExecutorImpl :
        SuspendExecutor<BoardsListStore.Intent,
                Action,
                BoardsListStore.State,
                Result,
                BoardsListStore.Label>() {

        override suspend fun executeAction(action: Action, getState: () -> BoardsListStore.State) =
            when (action) {
                Action.SubscribeToBoards -> {
                    db.selectAllBoardsFor(getState().chan).collect { list ->
                        dispatch(Result.BoardsList(list))
                    }
                }
                Action.CantUpdateBoards -> dispatch(Result.Error())
            }

        override suspend fun executeIntent(
            intent: BoardsListStore.Intent,
            getState: () -> BoardsListStore.State
        ) = when (intent) {
            is BoardsListStore.Intent.SelectFavorite -> {
                log("Favorite board selected: ${intent.id}")
            }
            is BoardsListStore.Intent.BoardSelected -> {
                log("Board selected: ${intent.id}")
                publish(BoardsListStore.Label.NavigateToThread(boardId = intent.id))
            }
            is BoardsListStore.Intent.SettingsSelected -> {
                log("Settings clicked")
                publish(BoardsListStore.Label.NavigateToSettings)
            }
        }
    }

    private inner class BootstrapperImpl : SuspendBootstrapper<Action>() {
        override suspend fun bootstrap() {
            dispatch(Action.SubscribeToBoards)
            loadAndSaveBoards()
        }

        private suspend fun loadAndSaveBoards() {
            // todo for current chan
            try {
                val response = api.getBoards()
                db.insetBoards(Chan.DvaCh, response.values.flatten())
            } catch (ex: Exception) {
                dispatch(Action.CantUpdateBoards)
            }
        }
    }

    private object ReducerImpl : Reducer<BoardsListStore.State, Result> {
        override fun BoardsListStore.State.reduce(result: Result): BoardsListStore.State =
            when (result) {
                is Result.BoardsList -> {
                    BoardsListStore.State(
                        boards = result.boards,
                        favorites = result.boards.filter { it.isFavorite },
                        isProgress = false,
                        chan = chan
                    )
                }
                is Result.Error -> {
                    BoardsListStore.State(
                        boards = boards,
                        favorites = favorites,
                        isProgress = false,
                        chan = chan
                    )
                }
            }
    }
}