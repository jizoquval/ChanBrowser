package com.jizoquval.chanBrowser.shared.board

import co.touchlab.kermit.Kermit
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.jizoquval.chanBrowser.shared.board.BoardStore.Intent
import com.jizoquval.chanBrowser.shared.board.BoardStore.Label
import com.jizoquval.chanBrowser.shared.board.BoardStore.State
import com.jizoquval.chanBrowser.shared.cache.ThreadPost
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.cache.repository.thread.IThreadRepository
import com.jizoquval.chanBrowser.shared.network.dvach.IDvachApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class BoardStoreFactory(
    private val boardId: Long,
    private val storeFactory: StoreFactory,
    private val api: IDvachApi,
    private val db: IThreadRepository,
    private val logger: Kermit
) : KoinComponent {

    private sealed class Action {
        object SubscribeToThreads : Action()
        object LoadThreads: Action()
    }

    private sealed class Result {
        data class ThreadsLoaded(val list: List<ThreadPost>) : Result()
        data class ThreadName(val name: String): Result()
    }

    fun create(): BoardStore = object :
        BoardStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "BoardStore",
            initialState = State(
                boardName = "",
                isProgress = true
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class BootstrapperImpl : SuspendBootstrapper<Action>() {
        override suspend fun bootstrap() {
            dispatch(Action.SubscribeToThreads)
            dispatch(Action.LoadThreads)
        }
    }

    private inner class ExecutorImpl : SuspendExecutor<Intent, Action, State, Result, Label>() {

        override suspend fun executeAction(action: Action, getState: () -> State) =
            when (action) {
                is Action.SubscribeToThreads -> {
                    db.selectThreads(
                        boardId = boardId,
                    ).collect {
                        dispatch(Result.ThreadsLoaded(it))
                    }
                }
                is Action.LoadThreads -> {
                    try {
                        val boardIdOnChan = db.selectBoardIdOnChan(boardId)
                        dispatch(Result.ThreadName(boardIdOnChan))

                        val response = api.getThreads(boardIdOnChan)
                        db.insert(boardId = boardId, threadJson = response)
                    } catch (ex: Exception) {
                        logger.e { "Get api exception: $ex" }
                    }
                }
            }

        override suspend fun executeIntent(
            intent: Intent,
            getState: () -> State
        ) = when (intent) {
            is Intent.SelectThread -> {
            }
        }
    }

    private object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (result) {
                is Result.ThreadsLoaded -> {
                    State(
                        boardName = boardName,
                        isProgress = false,
                        threads = result.list
                    )
                }
                is Result.ThreadName -> {
                    State(
                        boardName = result.name,
                        isProgress = isProgress,
                        threads = threads
                    )
                }
            }
    }
}
