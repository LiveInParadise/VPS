package com.vps.android.presentation.auth

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.vps.android.MainNavigationDirections
import com.vps.android.core.local.PrefManager
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.presentation.auth.feature.AuthEffectHandler
import com.vps.android.presentation.auth.feature.AuthFeature
import com.vps.android.presentation.base.BaseViewModel
import com.vps.android.presentation.base.NavigationCommand
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class AuthViewModel(
    private val authInteractor: AuthInteractor,
    private val pref: PrefManager,
) : BaseViewModel() {

    private val feature = AuthFeature()
    val state = feature.state

    protected val _messages: Channel<String> = Channel()
    protected val _events: Channel<AuthFeature.Event> = Channel()
    val events: Flow<AuthFeature.Event> = _events.receiveAsFlow()

    init {
        feature.init(viewModelScope, AuthEffectHandler(authInteractor, _events, _messages))
    }

    fun login(pinCode: String) {
        feature.act(AuthFeature.Action.Auth(pinCode))
    }

    fun navigateToSettings() {
        val dir = MainNavigationDirections.actionToMechanismType()
        navigate(NavigationCommand.Dir(dir))
    }

    fun onSave(outState: Bundle) {
        feature.save(outState)
    }

    fun onRestore(savedInstanceState: Bundle?) {
        feature.restore(savedInstanceState)
    }

}
