package com.jizoquval.chanBrowser.shared.koin

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.jizoquval.chanBrowser.shared.cache.AppDatabase
import com.jizoquval.chanBrowser.shared.cache.Attachment
import com.jizoquval.chanBrowser.shared.cache.Board
import com.jizoquval.chanBrowser.shared.cache.dbWrapper.board.BoardDb
import com.jizoquval.chanBrowser.shared.cache.dbWrapper.board.IBoardDb
import com.jizoquval.chanBrowser.shared.cache.dbWrapper.thread.IThreadDb
import com.jizoquval.chanBrowser.shared.cache.dbWrapper.thread.ThreadDb
import com.jizoquval.chanBrowser.shared.features.boardsList.BoardsListStoreFactory
import com.jizoquval.chanBrowser.shared.features.boardsList.ChanBoardsRepository
import com.jizoquval.chanBrowser.shared.features.boardsList.dvaCh.DvaChBoardsRepository
import com.jizoquval.chanBrowser.shared.features.threadsList.ThreadRepository
import com.jizoquval.chanBrowser.shared.features.threadsList.ThreadsListStoreFactory
import com.jizoquval.chanBrowser.shared.features.threadsList.dvaCh.DvaChThreadsRepository
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
            AttachmentAdapter = Attachment.Adapter(
                typeAdapter = EnumColumnAdapter()
            )
        )
    }
    single<IBoardDb> {
        BoardDb(
            database = get(),
            logger = getWith("BoardRepository"),
            backgroundDispatcher = get(named("ioDispatcher")),
        )
    }
    single<IThreadDb> {
        ThreadDb(
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
    single<ChanBoardsRepository> {
        DvaChBoardsRepository(
            api = get(),
            db = get()
        )
    }
    single<ThreadRepository> {
        DvaChThreadsRepository(
            api = get(),
            db = get()
        )
    }
    factory {
        BoardsListStoreFactory(
            storeFactory = DefaultStoreFactory,
            repository = get(),
            logger = getWith("BoardsListStoreFactory")
        ).create()
    }
    factory { (boardId: Long) ->
        ThreadsListStoreFactory(
            boardId = boardId,
            storeFactory = DefaultStoreFactory,
            repository = get(),
            logger = getWith("BoardStoreFactory")
        ).create()
    }
}

expect val platformModule: Module

internal inline fun <reified T> Scope.getWith(vararg params: Any?): T {
    return get { parametersOf(*params) }
}
