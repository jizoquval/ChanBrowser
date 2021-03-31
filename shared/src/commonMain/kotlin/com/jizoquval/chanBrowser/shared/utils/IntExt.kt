package com.jizoquval.chanBrowser.shared.utils

fun Int.toBoolean(): Boolean {
    return when (this) {
        0 -> false
        1 -> true
        else -> {
            throw IllegalStateException("Int must be in [0,1] range. Current value $this")
        }
    }
}