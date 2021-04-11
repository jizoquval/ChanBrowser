package com.jizoquval.chanBrowser.shared.koin

import androidx.sqlite.db.SupportSQLiteDatabase
import co.touchlab.kermit.Kermit
import co.touchlab.kermit.LogcatLogger
import com.jizoquval.chanBrowser.shared.cache.AppDatabase
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = get(),
            name = "App.db",
            callback = object : AndroidSqliteDriver.Callback(AppDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.execSQL("PRAGMA foreign_keys=ON")
                }
            }
        )
    }
    single(named("ioDispatcher")) { Dispatchers.IO }

    single<Settings> { AndroidSettings(get()) }

    val baseKermit = Kermit(LogcatLogger()).withTag("ChanBrowser")
    factory { (tag: String?) -> if (tag != null) baseKermit.withTag(tag) else baseKermit }
}
