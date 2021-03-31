package com.jizoquval.chanBrowser.androidApp

import android.app.Application
import android.content.Context
import android.util.Log
import com.jizoquval.chanBrowser.shared.koin.initKoin
import com.jizoquval.chanBrowser.shared.logger.ILogger
import com.jizoquval.chanBrowser.shared.viewModel.BoardViewModel
import com.jizoquval.chanBrowser.shared.viewModel.BoardsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

class BoardsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(
            appModule = module {
                single<Context> { this@BoardsApp }
                single {
                    ILogger { message -> Log.d("[Shared]", message) }
                }
                viewModel {
                    BoardsListViewModel(get())
                }
                viewModel { (boardId: String) ->
                    BoardViewModel(get { parametersOf(boardId) })
                }
            }
        )
    }
}
