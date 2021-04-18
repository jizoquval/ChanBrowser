package com.jizoquval.chanBrowser.shared.features.threadsList.model

data class BoardModel(
    val boardName: String,
    val isProgress: Boolean,
    val threadList: List<Thread>
) {
    data class Thread(
        val id: Long,
        val title: String,
        val message: String,
        val imgUrl: String,
        val postsCount: Int,
        val filesCount: Int
    )
}
