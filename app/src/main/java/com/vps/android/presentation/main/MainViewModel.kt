package com.vps.android.presentation.main

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.vps.android.MainNavigationDirections
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.presentation.base.BaseViewModel
import com.vps.android.presentation.base.NavigationCommand
import com.vps.android.presentation.main.feature.MainEffectHandler
import com.vps.android.presentation.main.feature.MainFeature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class MainViewModel(
    private val mechanismInteractor: MechanismInteractor,
    private val authInteractor: AuthInteractor,
) : BaseViewModel() {

    private val feature = MainFeature()
    val state = feature.state

    protected val _messages: Channel<String> = Channel()
    protected val _events: Channel<MainFeature.Event> = Channel()
    val events: Flow<MainFeature.Event> = _events.receiveAsFlow()

    init {
        feature.init(viewModelScope, MainEffectHandler(mechanismInteractor, authInteractor, _events, _messages))
    }

    override fun logout() {
        feature.act(MainFeature.Action.Logout)
    }

    fun startMechanismService() {
        feature.act(MainFeature.Action.StartMechanismService)
    }

    fun toAuthScreen() {
        val dir = MainNavigationDirections.actionLogout()
        navigate(NavigationCommand.Dir(dir))
    }

    fun openServiceScreen() {
        val dir = MainNavigationDirections.actionToService()
        navigate(NavigationCommand.Dir(dir))
    }

    fun onSave(outState: Bundle) {
        feature.save(outState)
    }

    fun onRestore(savedInstanceState: Bundle?) {
        feature.restore(savedInstanceState)
    }

}
