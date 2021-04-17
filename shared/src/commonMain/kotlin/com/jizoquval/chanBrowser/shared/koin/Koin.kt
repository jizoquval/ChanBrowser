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
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
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
        )
    }
    single<IBoardRepository> {
        BoardRepository(
            database = get(),
            logger = getWith("BoardRepository"),
            backgroundDispatcher = get(named("ioDispatcher")),
        )
    }
    single<IThreadRepository> {
        ThreadRepository(
            database = get(),
            logger = getWith("ThreadRepository"),
            backgroundDispatcher = get(named("ioDispatcher")),
        )
    }
    single(named(Endpoint.DvaCh)) {
        createHttpClient(Endpoint.DvaCh, getWith("Network"))
    }
    single(named(Endpoint.FourChan)) {
        createHttpClient(Endpoint.FourChan, getWith("Network"))
    }
    single<IDvachApi> {
        DvachApi(get(named(Endpoint.DvaCh)), get(named("ioDispatcher")))
    }
    factory {
        BoardsListStoreFactory(
            storeFactory = DefaultStoreFactory,
            api = get(),
            db = get(),
            logger = getWith("BoardsListStoreFactory")
        ).create()
    }
    factory { (boardId: Long) ->
        BoardStoreFactory(
            boardId = boardId,
            storeFactory = DefaultStoreFactory,
            api = get(),
            db = get(),
            logger = getWith("BoardStoreFactory")
        ).create()
    }
}

expect val platformModule: Module

internal inline fun <reified T> Scope.getWith(vararg params: Any?): T {
    return get { parametersOf(*params) }
}
