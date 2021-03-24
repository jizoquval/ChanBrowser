package com.jizoquval.chanBrowser.shared.viewModel

import com.jizoquval.chanBrowser.shared.boardsList.BoardsListStore
import com.jizoquval.chanBrowser.shared.boardsList.model.BoardListEvent
import com.jizoquval.chanBrowser.shared.boardsList.model.BoardListModel
import com.jizoquval.chanBrowser.shared.boardsList.model.eventToIntent
import com.jizoquval.chanBrowser.shared.boardsList.model.stateToModel

class BoardsListViewModel(
    store: BoardsListStore
) : BaseViewModel<BoardListModel, BoardListEvent, BoardsListStore.Intent, BoardsListStore.State>(
    store = store,
    stateMapper = ::stateToModel,
    intentMapper = ::eventToIntent
)