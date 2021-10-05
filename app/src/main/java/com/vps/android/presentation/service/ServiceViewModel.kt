package com.vps.android.presentation.service

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.vps.android.MainNavigationDirections
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.presentation.base.BaseViewModel
import com.vps.android.presentation.base.NavigationCommand
import com.vps.android.presentation.service.feature.ServiceEffectHandler
import com.vps.android.presentation.service.feature.ServiceFeature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class ServiceViewModel(
    private val mechanismInteractor: MechanismInteractor,
    private val authInteractor: AuthInteractor,
) : BaseViewModel() {

    private val feature = ServiceFeature()
    val state = feature.state

    protected val _messages: Channel<String> = Channel()
    protected val _events: Channel<ServiceFeature.Event> = Channel()
    val events: Flow<ServiceFeature.Event> = _events.receiveAsFlow()

    init {
        feature.init(viewModelScope, ServiceEffectHandler(mechanismInteractor, authInteractor, _events, _messages))
    }

    override fun logout() {
        feature.act(ServiceFeature.Action.Logout)
    }

    fun stopMechanismService() {
        feature.act(ServiceFeature.Action.StopMechanismService)
    }

    fun toAuthScreen() {
        val dir = MainNavigationDirections.actionLogout()
        navigate(NavigationCommand.Dir(dir))
    }

    fun openMainScreen() {
        val dir = MainNavigationDirections.actionToMain()
        navigate(NavigationCommand.Dir(dir))
    }

    fun onSave(outState: Bundle) {
        feature.save(outState)
    }

    fun onRestore(savedInstanceState: Bundle?) {
        feature.restore(savedInstanceState)
    }

}
