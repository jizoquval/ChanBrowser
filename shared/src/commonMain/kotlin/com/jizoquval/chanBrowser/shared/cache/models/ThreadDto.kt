package com.jizoquval.chanBrowser.shared.cache.models

data class ThreadDto(
    val boardId: Long,
    val threadChanId: Long,
    val maxMsgLength: Int,
    val postsCount: Int,
    val filesCount: Int,
    val timestamp: Long,
    val isEndless: Boolean,
    val isClosed: Boolean,
    val isArchived: Boolean = false,
    val post: PostDto
)
