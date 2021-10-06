package com.vps.android.presentation.mechanism_type

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.vps.android.MainNavigationDirections
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.presentation.base.BaseViewModel
import com.vps.android.presentation.base.NavigationCommand
import com.vps.android.presentation.mechanism.MechanismSpec
import com.vps.android.presentation.mechanism_type.feature.MechanismTypeEffectHandler
import com.vps.android.presentation.mechanism_type.feature.MechanismTypeFeature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class MechanismTypeViewModel(
    private val mechanismInteractor: MechanismInteractor,
    private val authInteractor: AuthInteractor,
) : BaseViewModel() {

    private val feature = MechanismTypeFeature()
    val state = feature.state

    protected val _messages: Channel<String> = Channel()
    protected val _events: Channel<MechanismTypeFeature.Event> = Channel()
    val events: Flow<MechanismTypeFeature.Event> = _events.receiveAsFlow()

    init {
        feature.init(viewModelScope, MechanismTypeEffectHandler(mechanismInteractor, authInteractor, _events, _messages))
        getMechanismTypes()
    }

    private fun getMechanismTypes() {
        feature.act(MechanismTypeFeature.Action.GetMechanismTypes)
    }

    override fun logout() {
        feature.act(MechanismTypeFeature.Action.Logout)
    }

    fun toAuthScreen() {
        val dir = MainNavigationDirections.actionLogout()
        navigate(NavigationCommand.Dir(dir))
    }

    fun openChooseMechanismScreen(typeId: Int) {
        val spec = MechanismSpec(typeId)
        val dir = MainNavigationDirections.actionToMechanism(spec)
        navigate(NavigationCommand.Dir(dir))
    }

    fun onSave(outState: Bundle) {
        feature.save(outState)
    }

    fun onRestore(savedInstanceState: Bundle?) {
        feature.restore(savedInstanceState)
    }

}
