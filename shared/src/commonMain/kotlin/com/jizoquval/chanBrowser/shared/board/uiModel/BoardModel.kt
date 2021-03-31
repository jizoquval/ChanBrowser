package com.jizoquval.chanBrowser.shared.board.uiModel

data class BoardModel(
    val boardName: String,
    val isProgress: Boolean,
    val threadList: List<Thread>
) {
    data class Thread(
        val id: Long,
        val title: String,
        val message: String,
        val postsCount: Int,
        val filesCount: Int
    )
}
