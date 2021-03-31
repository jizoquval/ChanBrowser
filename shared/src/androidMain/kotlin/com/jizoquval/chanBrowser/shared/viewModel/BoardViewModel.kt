package com.jizoquval.chanBrowser.shared.viewModel

import com.jizoquval.chanBrowser.shared.board.BoardStore
import com.jizoquval.chanBrowser.shared.board.uiModel.BoardModel
import com.jizoquval.chanBrowser.shared.board.uiModel.stateToModel

class BoardViewModel(
    store: BoardStore
):BaseViewModel<BoardModel, Any, BoardStore.Intent, BoardStore.State, BoardStore.Label>(
    store = store,
    stateMapper = ::stateToModel,
    intentMapper = {
        BoardStore.Intent.SelectThread("2")
    }
)