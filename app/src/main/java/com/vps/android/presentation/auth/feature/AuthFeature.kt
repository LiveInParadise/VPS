package com.vps.android.presentation.auth.feature

import com.vps.android.presentation.base.BaseFeature

class AuthFeature : BaseFeature<AuthState, AuthFeature.Action, AuthFeature.Effect>() {

    override fun initialState(): AuthState = AuthState()
    override fun initialEffs(): Set<Effect> = emptySet()

    sealed class Action {
        data class Auth(val number: String, val password: String) : Action()

        data class Error(val error: Throwable) : Action()
    }

    sealed class Effect {
        data class Auth(val number: String, val password: String) : Effect()

        data class DispatchEvent(val event: Event) : Effect()
        data class DispatchMessage(val message: String) : Effect()
    }

    sealed class Event {

        data class Error(val error: Throwable) : Event()
    }

}
