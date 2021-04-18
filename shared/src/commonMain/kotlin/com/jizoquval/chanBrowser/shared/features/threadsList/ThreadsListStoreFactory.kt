package com.jizoquval.chanBrowser.shared.features.threadsList

import co.touchlab.kermit.Kermit
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.jizoquval.chanBrowser.shared.cache.ThreadPost
import com.jizoquval.chanBrowser.shared.features.threadsList.BoardStore.Intent
import com.jizoquval.chanBrowser.shared.features.threadsList.BoardStore.Label
import com.jizoquval.chanBrowser.shared.features.threadsList.BoardStore.State
import kotlinx.coroutines.flow.collect
import org.koin.core.component.KoinComponent

class ThreadsListStoreFactory(
    private val boardId: Long,
    private val repository: ThreadRepository,
    private val storeFactory: StoreFactory,
    private val logger: Kermit
) : KoinComponent {

    private sealed class Action {
        object SubscribeToThreads : Action()
        object LoadThreads : Action()
        object GetBoardName : Action()
    }

    private sealed class Result {
        data class ThreadsLoaded(val list: List<ThreadPost>) : Result()
        data class BoardName(val name: String) : Result()
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
            dispatch(Action.GetBoardName)
            dispatch(Action.SubscribeToThreads)
            dispatch(Action.LoadThreads)
        }
    }

    private inner class ExecutorImpl : SuspendExecutor<Intent, Action, State, Result, Label>() {

        override suspend fun executeAction(action: Action, getState: () -> State) =
            when (action) {
                is Action.SubscribeToThreads -> {
                    repository.subscribeToThreads(boardId).collect { list ->
                        dispatch(Result.ThreadsLoaded(list))
                    }
                }
                is Action.LoadThreads -> {
                    try {
                        repository.loadThreads(boardId)
                    } catch (ex: Exception) {
                        logger.e { "Get api exception: $ex" }
                    }
                }
                is Action.GetBoardName -> {
                    val boardName = repository.getBoardName(boardId)
                    dispatch(Result.BoardName(boardName))
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
                is Result.BoardName -> {
                    State(
                        boardName = result.name,
                        isProgress = isProgress,
                        threads = threads
                    )
                }
            }
    }
}
