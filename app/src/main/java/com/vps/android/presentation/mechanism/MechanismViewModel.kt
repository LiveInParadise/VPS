package com.vps.android.presentation.mechanism

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.vps.android.MainNavigationDirections
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.presentation.base.BaseViewModel
import com.vps.android.presentation.base.NavigationCommand
import com.vps.android.presentation.mechanism.feature.MechanismEffectHandler
import com.vps.android.presentation.mechanism.feature.MechanismFeature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class MechanismViewModel(
    private val mechanismInteractor: MechanismInteractor,
    private val authInteractor: AuthInteractor,
) : BaseViewModel() {

    private val feature = MechanismFeature()
    val state = feature.state

    protected val _messages: Channel<String> = Channel()
    protected val _events: Channel<MechanismFeature.Event> = Channel()
    val events: Flow<MechanismFeature.Event> = _events.receiveAsFlow()

    init {
        feature.init(viewModelScope, MechanismEffectHandler(mechanismInteractor, authInteractor, _events, _messages))
        getMechanismList()
    }

    private fun getMechanismList() {
        feature.act(MechanismFeature.Action.GetCombinedMechanismList)
    }

    override fun logout() {
        feature.act(MechanismFeature.Action.Logout)
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
