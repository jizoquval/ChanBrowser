package com.jizoquval.chanBrowser.shared.boardsList.model

sealed class BoardListEvent {
    class BoardSelected(val name: String) : BoardListEvent()
}


