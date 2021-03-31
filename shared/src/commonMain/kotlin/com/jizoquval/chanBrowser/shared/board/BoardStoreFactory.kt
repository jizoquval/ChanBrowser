package com.jizoquval.chanBrowser.shared.board

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.jizoquval.chanBrowser.shared.board.BoardStore.Intent
import com.jizoquval.chanBrowser.shared.board.BoardStore.State
import com.jizoquval.chanBrowser.shared.board.BoardStore.Label
import com.jizoquval.chanBrowser.shared.cache.ThreadPost
import com.jizoquval.chanBrowser.shared.cache.models.Chan
import com.jizoquval.chanBrowser.shared.cache.repository.thread.IThreadRepository
import com.jizoquval.chanBrowser.shared.logger.LogLevel
import com.jizoquval.chanBrowser.shared.logger.log
import com.jizoquval.chanBrowser.shared.network.dvach.IDvachApi
import io.ktor.client.features.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class BoardStoreFactory(
    private val boardId: String,
    private val storeFactory: StoreFactory
) : KoinComponent {

    private val api: IDvachApi by inject()
    private val db: IThreadRepository by inject()

    private sealed class Action {
        object SubscribeToThreads : Action()
        object LoadThreads : Action()
    }

    private sealed class Result {
       data class ThreadsLoaded(val list: List<ThreadPost>) : Result()
    }


    fun create(): BoardStore = object : BoardStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "BoardStore",
            initialState = State(
                boardName = boardId,
                isProgress = true
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class BootstrapperImpl : SuspendBootstrapper<Action>() {
        override suspend fun bootstrap() {
            loadAndSaveThreads()
            dispatch(Action.SubscribeToThreads)
        }

        private suspend fun loadAndSaveThreads() {
            try {
                // todo for current chan
                val response = api.getThreads(boardId)
                db.insert(chan = Chan.DvaCh, boardId = boardId, threadJson = response)
            } catch (ex: Exception) {
                log(LogLevel.ERROR, "Get api exception: $ex")
            }
        }
    }

    private inner class ExecutorImpl : SuspendExecutor<Intent, Action, State, Result, Label>() {

        override suspend fun executeAction(action: Action, getState: () -> State) =
            when (action) {
                Action.SubscribeToThreads -> {
                    db.selectThreads(
                        chan = Chan.DvaCh,
                        boardId = boardId,
                    ).collect {
                        dispatch(Result.ThreadsLoaded(it))
                    }
                }
                is Action.LoadThreads -> {

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
            }
    }
}