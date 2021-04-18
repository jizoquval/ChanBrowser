package com.jizoquval.chanBrowser.shared.cache.models

enum class Attachment(private val code: Long) {
    JPEG(1), PNG(2), GIF(4), WEBM(6),
    MP4(10), STICKER_PNG(100), UNKNOWN(-1);

    companion object {
        fun byCode(code: Long): Attachment {
            return values().find { it.code == code } ?: UNKNOWN
        }
    }
}
