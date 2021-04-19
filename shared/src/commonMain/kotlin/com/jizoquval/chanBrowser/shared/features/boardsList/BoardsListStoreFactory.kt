package com.jizoquval.chanBrowser.shared.features.boardsList

import co.touchlab.kermit.Kermit
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import kotlinx.coroutines.flow.collect
import org.koin.core.component.KoinComponent

class BoardsListStoreFactory(
    private val repository: ChanBoardsRepository,
      private val storeFactory: StoreFactory,
    private val logger: Kermit
) : KoinComponent {
    var x = 2

    private sealed class Action {
        object SubscribeToBoards : Action()
        object CantUpdateBoards : Action()
    }

    private sealed class Result {
        class BoardsList(val boards: List<Board>) : Result()
        class Error(val message: String = "No Internet") : Result()
    }

    fun create(): BoardsListStore =
        object :
            BoardsListStore,
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
                    repository.subscribeToBoards().collect { list ->
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
                logger.d { "Favorite board selected: ${intent.id}" }
                // TODO change to opposite value
                repository.setFavorite(boardId = intent.id, true)
            }
            is BoardsListStore.Intent.BoardSelected -> {
                logger.d { "Board selected: ${intent.id}" }
                publish(BoardsListStore.Label.NavigateToThread(boardId = intent.id))
            }
            is BoardsListStore.Intent.SettingsSelected -> {
                logger.d { "Settings clicked" }
                publish(BoardsListStore.Label.NavigateToSettings)
            }
        }
    }

    private inner class BootstrapperImpl : SuspendBootstrapper<Action>() {
        override suspend fun bootstrap() {
            dispatch(Action.SubscribeToBoards)
            try {
                repository.loadBoards()
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
