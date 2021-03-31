package com.jizoquval.chanBrowser.shared.logger

import com.jizoquval.chanBrowser.shared.koin.getKoinInstance

fun interface ILogger {
    fun log(message: String)
}

private val logger: ILogger = getKoinInstance()

fun log(level: LogLevel, tag: String, message: String, error: Throwable? = null) {
    with(logger) {
        val prefix = "::$level::[$tag]"
        log("$prefix $message")
        if (error != null) {
            log("[$tag] Error: $error, Message: ${error.message}]")
        }
    }
}

fun log(message: String, error: Throwable? = null) {
    log(
        level = LogLevel.INFO,
        tag = "no tag",
        message = message,
        error = error
    )
}

fun Any.log(message: String, error: Throwable? = null) {
    log(
        level = LogLevel.INFO,
        tag = this::class.simpleName.orEmpty(),
        message = message,
        error = error
    )
}

fun Any.log(level: LogLevel, message: String, error: Throwable? = null) {
    log(
        level = level,
        tag = this::class.simpleName.orEmpty(),
        message = message,
        error = error
    )
}

enum class LogLevel {
    ALL,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    ASSERT;
}
