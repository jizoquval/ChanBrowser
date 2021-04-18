package com.jizoquval.chanBrowser.shared.network.dvach.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostJson(
    @SerialName("num")
    val id: Int,
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
    @SerialName("banned")
    val isAuthorBanned: Int,
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
)
