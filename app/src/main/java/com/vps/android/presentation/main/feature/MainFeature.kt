package com.vps.android.presentation.main.feature

import com.vps.android.presentation.base.BaseFeature

class MainFeature : BaseFeature<MainState, MainFeature.Action, MainFeature.Effect>() {

    override fun initialState(): MainState = MainState()
    override fun initialEffs(): Set<Effect> = emptySet()

    sealed class Action {
        object Logout : Action()
        object LogoutComplete : Action()

        object StartMechanismService : Action()
        object StartMechanismServiceComplete : Action()

        data class Error(val error: Throwable) : Action()
    }

    sealed class Effect {
        object Logout : Effect()

        object StartMechanismService : Effect()

        data class DispatchEvent(val event: Event) : Effect()
        data class DispatchMessage(val message: String) : Effect()
    }

    sealed class Event {
        object Logout : Event()

        object StartMechanismServiceComplete : Event()

        data class Error(val error: Throwable) : Event()
    }

}
