package com.jizoquval.chanBrowser.shared.cache.models

data class PostDto(
    val postChanId: Long,
    val title: String,
    val message: String,
    val date: String,
    val timestamp: Long,
    val isOP: Boolean,
    val isSticky: Int,
    val authorName: String,
    val authorEmail: String,
    val likesCount: Int?,
    val dislikesCount: Int?
)
