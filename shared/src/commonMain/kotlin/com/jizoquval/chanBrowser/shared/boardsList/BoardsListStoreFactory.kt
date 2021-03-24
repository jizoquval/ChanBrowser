package com.jizoquval.chanBrowser.shared.boardsList

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.Database
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.logger.LogLevel
import com.jizoquval.chanBrowser.shared.logger.log
import com.jizoquval.chanBrowser.shared.network.IDvachApi
import io.ktor.client.features.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BoardsListStoreFactory(private val storeFactory: StoreFactory) : KoinComponent {

    private val api: IDvachApi by inject()
    private val db: Database by inject()

    private sealed class Action {
        object SubscribeToBoards : Action()
    }

    private sealed class Result {
        class BoardsList(val boards: List<Board>) : Result()
    }

    fun create(): BoardsListStore =
        object : BoardsListStore,
            Store<BoardsListStore.Intent, BoardsListStore.State, Nothing> by storeFactory.create(
                name = "BoardsListStore",
                initialState = BoardsListStore.State(
                    favorites = null,
                    boards = null,
                    isProgress = true
                ),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private inner class ExecutorImpl :
        SuspendExecutor<BoardsListStore.Intent, Action, BoardsListStore.State, Result, Nothing>() {

        override suspend fun executeAction(action: Action, getState: () -> BoardsListStore.State) =
            when (action) {
                Action.SubscribeToBoards -> {
                    db.selectAllBoardsFor(Chan._2CH).collect { list ->
                        dispatch(Result.BoardsList(list))
                    }
                }
            }

        override suspend fun executeIntent(
            intent: BoardsListStore.Intent,
            getState: () -> BoardsListStore.State
        ) = when (intent) {
            is BoardsListStore.Intent.BoardSelected -> {
                log("Board selected: ${intent.name}")
                try {
                    val response = withContext(Dispatchers.Default) {
                        api.getThreads(intent.name)
                    }
                    log("Response: $response")
                } catch (ex: ResponseException) {
                    log(LogLevel.ERROR, "Get api exception: $ex")
                }
            }
        }
    }

    private inner class BootstrapperImpl : SuspendBootstrapper<Action>() {
        override suspend fun bootstrap() {
            dispatch(Action.SubscribeToBoards)
            loadAndSaveBoards()
        }

        private suspend fun loadAndSaveBoards() {
            try {
                val response = withContext(Dispatchers.Default) { api.getBoards() }
                db.insetBoards(Chan._2CH, response.values.flatten())
            } catch (ex: ResponseException) {
                log(LogLevel.ERROR, "Get api exception: $ex")
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
                        isProgress = false
                    )
                }
            }
    }
}