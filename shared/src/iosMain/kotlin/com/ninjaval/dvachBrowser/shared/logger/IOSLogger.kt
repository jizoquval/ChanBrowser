package com.ninjaval.dvachBrowser.shared.logger

import kotlin.native.concurrent.AtomicReference

@SharedImmutable
private val loggerInstance: AtomicReference<ILogger?> = AtomicReference(null)

internal actual fun getLoggerInstance(): ILogger? = loggerInstance.value

internal actual fun initLoggerInternal(logger: ILogger) {
    loggerInstance.value = logger
}
