package com.vps.android.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {

    fun main(): CoroutineDispatcher

    fun io(): CoroutineDispatcher

    fun default(): CoroutineDispatcher
}
