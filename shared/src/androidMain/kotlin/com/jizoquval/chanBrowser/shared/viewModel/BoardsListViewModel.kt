package com.jizoquval.chanBrowser.shared.viewModel

import com.jizoquval.chanBrowser.shared.features.boardsList.BoardsListStore
import com.jizoquval.chanBrowser.shared.features.boardsList.model.BoardListEvent
import com.jizoquval.chanBrowser.shared.features.boardsList.model.BoardListModel
import com.jizoquval.chanBrowser.shared.features.boardsList.model.eventToIntent
import com.jizoquval.chanBrowser.shared.features.boardsList.model.stateToModel

class BoardsListViewModel(
    store: BoardsListStore
) : BaseViewModel<BoardListModel, BoardListEvent, BoardsListStore.Intent,
    BoardsListStore.State, BoardsListStore.Label>(
    store = store,
    stateMapper = ::stateToModel,
    intentMapper = ::eventToIntent
)
