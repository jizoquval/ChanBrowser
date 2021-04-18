package com.jizoquval.chanBrowser.shared.viewModel

import com.jizoquval.chanBrowser.shared.features.threadsList.BoardStore
import com.jizoquval.chanBrowser.shared.features.threadsList.model.BoardModel
import com.jizoquval.chanBrowser.shared.features.threadsList.model.stateToModel

class BoardViewModel(
    store: BoardStore
) : BaseViewModel<BoardModel, Any, BoardStore.Intent, BoardStore.State, BoardStore.Label>(
    store = store,
    stateMapper = ::stateToModel,
    intentMapper = {
        BoardStore.Intent.SelectThread("2")
    }
)
