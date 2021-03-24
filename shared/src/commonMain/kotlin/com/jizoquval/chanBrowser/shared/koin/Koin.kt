package com.jizoquval.chanBrowser.shared.koin

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.jizoquval.chanBrowser.shared.boardsList.BoardsListStoreFactory
import com.jizoquval.chanBrowser.shared.cache.Database
import com.jizoquval.chanBrowser.shared.network.DvachApi
import com.jizoquval.chanBrowser.shared.network.IDvachApi
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.module.Module
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
        Database(
            sdlDriver = get(),
            backgroundDispatcher = Dispatchers.Default
        )
    }
    single<IDvachApi> {
        DvachApi()
    }
    factory {
        BoardsListStoreFactory(DefaultStoreFactory).create()
    }
}

expect val platformModule: Module

inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}