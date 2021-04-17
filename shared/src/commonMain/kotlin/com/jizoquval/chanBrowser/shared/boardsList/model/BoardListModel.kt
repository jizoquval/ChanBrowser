package com.jizoquval.chanBrowser.shared.boardsList.model

data class BoardListModel(
    val chan: String,
    val categories: Map<String, List<Board>>,
    val favorites: List<Board>,
    val isProgress: Boolean
) {
    data class Board(
        val id: Long,
        val name: String,
        val description: String
    )

    data class Category(
        val category: String,
        val boards: List<Board>
    )
}
