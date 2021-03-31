package com.jizoquval.chanBrowser.shared.koin

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.jizoquval.chanBrowser.shared.board.BoardStoreFactory
import com.jizoquval.chanBrowser.shared.boardsList.BoardsListStoreFactory
import com.jizoquval.chanBrowser.shared.cache.AppDatabase
import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.Post
import com.jizoquval.chanBrowser.shared.cache.Thread
import com.jizoquval.chanBrowser.shared.cache.repository.board.BoardRepository
import com.jizoquval.chanBrowser.shared.cache.repository.board.IBoardRepository
import com.jizoquval.chanBrowser.shared.cache.repository.thread.IThreadRepository
import com.jizoquval.chanBrowser.shared.cache.repository.thread.ThreadRepository
import com.jizoquval.chanBrowser.shared.network.Endpoint
import com.jizoquval.chanBrowser.shared.network.createHttpClient
import com.jizoquval.chanBrowser.shared.network.dvach.DvachApi
import com.jizoquval.chanBrowser.shared.network.dvach.IDvachApi
import com.squareup.sqldelight.EnumColumnAdapter
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun initKoin(appModule: Module): KoinApplication = startKoin {
    modules(
        appModule,
        platformModule,
        coreModule
    )
}

private val coreModule = module {
    single {
        AppDatabase(
            get(),
            BoardAdapter = Board.Adapter(
                chanAdapter = EnumColumnAdapter()
            ),
            ThreadAdapter = Thread.Adapter(
                chanAdapter = EnumColumnAdapter()
            ),
            PostAdapter = Post.Adapter(
                chanAdapter = EnumColumnAdapter()
            )
        )
    }
    single<IBoardRepository> {
        BoardRepository(get(), get(named("ioDispatcher")))
    }
    single<IThreadRepository> {
        ThreadRepository(get(), get(named("ioDispatcher")))
    }
    single(named(Endpoint.DvaCh)) {
        createHttpClient(Endpoint.DvaCh)
    }
    single(named(Endpoint.FourChan)) {
        createHttpClient(Endpoint.FourChan)
    }
    single<IDvachApi> {
        DvachApi(get(named(Endpoint.DvaCh)), get(named("ioDispatcher")))
    }
    factory {
        BoardsListStoreFactory(DefaultStoreFactory).create()
    }
    factory { (boardId: String) ->
        BoardStoreFactory(boardId = boardId, DefaultStoreFactory).create()
    }
}

expect val platformModule: Module

inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}
