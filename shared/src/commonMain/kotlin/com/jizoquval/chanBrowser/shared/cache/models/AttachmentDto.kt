package com.jizoquval.chanBrowser.shared.cache.models

data class AttachmentDto(
    val url: String,
    val type: Attachment,
    val isForAdults: Boolean = false,
    val width: Int,
    val height: Int,
    val smallFUrl: String,
    val smallFWidth: Int,
    val smallFHeight: Int,
)
