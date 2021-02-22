package com.ninjaval.dvachBrowser.shared.logger

interface ILogger {
    fun log(message: String)
}

internal expect fun getLoggerInstance(): ILogger?

internal expect fun initLoggerInternal(logger: ILogger)

fun initLogger(logger: ILogger) {
    initLoggerInternal(logger)
}

fun log(level: LogLevel, tag: String, message: String, error: Throwable? = null) {
    getLoggerInstance()?.apply {
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

class StdLogger : ILogger {

    override fun log(message: String) {
        println(message)
    }
}

enum class LogLevel {
    ALL,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    ASSERT;
}
