package com.vps.android.presentation.base

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseFeature<S : Reducer<S, A, E>, A, E> : Feature<S, A, E> {

    protected val _state: MutableStateFlow<S> = MutableStateFlow(initialState())
    override val state: StateFlow<S>
        get() = _state.asStateFlow()

    private lateinit var _scope: CoroutineScope
    private val actions: MutableSharedFlow<A> = MutableSharedFlow()

    open fun save(outState: Bundle) {}
    open fun restore(savedState: Bundle?) {}

    override fun act(action: A) {
        _scope.launch {
            actions.emit(action)
        }
    }

    override fun init(scope: CoroutineScope, effHandler: EffHandler<E, A>) {
        _scope = scope
        _scope.launch {
            actions
                .scan(initialState() to initialEffs()) { (s, _), action ->
                    s.reduce(action)
                }
                .collect { (s, effs) ->
                    _state.emit(s)
                    effs.forEach {
                        launch {
                            effHandler.handle(it, ::act)
                        }
                    }
                }
        }
    }

}
