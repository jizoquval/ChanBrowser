package com.jizoquval.chanBrowser.shared.network.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThreadsListJson(
    @SerialName("max_comment")
    val lengthOfMessage: Int,
    @SerialName("threads")
    val threads: List<ThreadJson>
)

@Serializable
data class ThreadJson(
    @SerialName("banned")
    val isBanned: Int,
    @SerialName("num")
    val id: Long,
    @SerialName("subject")
    val subject: String,
    @SerialName("comment")
    val comment: String,
    @SerialName("posts_count")
    val postsCount: Int,
    @SerialName("files_count")
    val filesCount: Int,
    @SerialName("op")
    val op: Int,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("sticky")
    val isSticky: Int,
    @SerialName("closed")
    val isClosed: Int,
    @SerialName("endless")
    val isEndLess: Int,
    @SerialName("lasthit")
    val lastHit: Long,
    @SerialName("date")
    val date: String,
    @SerialName("name")
    val authorName: String,
    @SerialName("email")
    val authorEmail: String,
    @SerialName("likes")
    val likes: Int? = null,
    @SerialName("dislikes")
    val dislikes: Int? = null,
    @SerialName("files")
    val files: List<FileJson>
)
