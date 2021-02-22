package com.ninjaval.dvachBrowser.shared.logger

import java.util.concurrent.atomic.AtomicReference

private val loggerInstance: AtomicReference<ILogger?> = AtomicReference(null)

internal actual fun getLoggerInstance(): ILogger? = loggerInstance.get()

internal actual fun initLoggerInternal(logger: ILogger) {
    loggerInstance.set(logger)
}
