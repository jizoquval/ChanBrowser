package com.jizoquval.chanBrowser.shared.cache.models

enum class Attachment(private val code: Int) {
    JPEG(1), PNG(2), GIF(4), WEBM(6), MP4(10), STICKER_PNG(100);

    companion object {
        fun byCode(code: Int): Attachment {
            return values().find { it.code == code }
                ?: throw IllegalArgumentException("Invalid attachment code: $code")
        }
    }
}