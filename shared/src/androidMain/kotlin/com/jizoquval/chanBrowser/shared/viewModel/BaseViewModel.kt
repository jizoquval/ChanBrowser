package com.jizoquval.chanBrowser.shared.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

open class BaseViewModel<Model : Any, Event : Any, in Intent : Any, in State : Any, Label: Any>(
    private val store: Store<Intent, State, Label>,
    private val stateMapper: State.() -> Model,
    private val intentMapper: Event.() -> Intent
) : ViewModel() {

    @ExperimentalCoroutinesApi
    val state = store
        .states
        .flowOn(Dispatchers.Main)
        .map { stateMapper(it) }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, stateMapper(store.state))

    @ExperimentalCoroutinesApi
    val labels = store
        .labels
        .shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 0)

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }

    fun dispatch(event: Event) {
        store.accept(intentMapper(event))
    }
}