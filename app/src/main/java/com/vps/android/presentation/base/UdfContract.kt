package com.vps.android.presentation.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

/**
 * Feature interface with types: State, Action, Side Effect
 */
interface Feature<S, A, E> {
    fun initialState(): S
    fun initialEffs(): Set<E>
    fun act(action: A)
    fun init(scope: CoroutineScope, effHandler: EffHandler<E, A>)

//    fun save(outState: Bundle)
//    fun restore(savedState: Bundle?)

    val state: StateFlow<S>
}

/**
 * Reducer interface with types: State, Action, Side Effect
 */
interface Reducer<S, A, E> {
    suspend fun reduce(action: A): Pair<S, Set<E>>
}

/**
 * Side Effect with types: Effect, Action callback
 */
interface EffHandler<E, A> {
    suspend fun handle(effect: E, commit: (A) -> Unit)
}
