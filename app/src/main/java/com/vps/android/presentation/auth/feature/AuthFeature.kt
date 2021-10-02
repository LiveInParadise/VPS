package com.vps.android.presentation.auth.feature

import com.vps.android.presentation.base.BaseFeature

class AuthFeature : BaseFeature<AuthState, AuthFeature.Action, AuthFeature.Effect>() {

    override fun initialState(): AuthState = AuthState()
    override fun initialEffs(): Set<Effect> = emptySet()

    sealed class Action {
        data class Auth(val pinCode: String) : Action()
        object AuthComplete : Action()
        data class AuthError(val message: String?) : Action()

        data class Error(val error: Throwable) : Action()
    }

    sealed class Effect {
        data class Auth(val pinCode: String) : Effect()

        data class DispatchEvent(val event: Event) : Effect()
        data class DispatchMessage(val message: String) : Effect()
    }

    sealed class Event {
        object AuthSuccess : Event()

        data class Error(val error: Throwable) : Event()
    }

}
